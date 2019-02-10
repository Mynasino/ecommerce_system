package com.ecommerce.back.security;

import com.alibaba.fastjson.JSONObject;
import com.ecommerce.back.util.IOUtil;
import com.ecommerce.back.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@Component
public class JWTAuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
        //If it is not mapped to a method, pass directly
        if (!(object instanceof HandlerMethod)) return true;
        //check if the annotation AuthenticationRequired exists
        HandlerMethod handlerMethod = (HandlerMethod)object;
        Method method = handlerMethod.getMethod();
        //if not present, then pass directly
        if (!method.isAnnotationPresent(AuthenticationRequired.class))
            return true;
            //if present, then need authentication
        else {
            //get the annotation
            AuthenticationRequired authenticationRequired = method.getAnnotation(AuthenticationRequired.class);
            //get the authentication level required
            AuthenticationLevel[] authenticationLevelsRequired = authenticationRequired.levels();
            List<AuthenticationLevel> authenticationLevels = Arrays.asList(authenticationRequired.levels());

            //get jwtString from header
            String jwtString = request.getHeader(JWTUtil.HEADER_KEY);
            //no jwt
            if (jwtString == null)
                return SendErrorMessage(SC_UNAUTHORIZED, "login first", response);
            //try parse JWT
            PersonDetail personDetail;
            try {
                personDetail = JWTUtil.getPersonDetailByJWTString(jwtString);
            } catch (ExpiredJwtException e) {
                return SendErrorMessage(SC_UNAUTHORIZED, "JWT Expired, please relogin", response);
            } catch (Exception e) {
                return SendErrorMessage(SC_UNAUTHORIZED, "JWT illegal", response);
            }
            //jwt parse fail
            if (personDetail == null)
                return SendErrorMessage(SC_BAD_REQUEST, "fail parsing jwt in authentication", response);
            //authorities
            if (authenticationLevels.contains(personDetail.getAuthenticationLevel())) {
                //index of authentication level
                int authenIndex = authenticationLevels.indexOf(personDetail.getAuthenticationLevel());
                //if authority of specific individual is needed
                boolean specificIndividualNeeded = authenticationRequired.specifics()[authenIndex];
                if (!specificIndividualNeeded)
                    return true;
                else {
                    String specificIndividualName = request.getParameter(JWTUtil.SPECIFIC_PARAM_NAME);
                    return personDetail.getName().equals(specificIndividualName) ||
                            SendErrorMessage(SC_FORBIDDEN, "need authority of "
                                    + personDetail.getAuthenticationLevel().name + " with name: "
                                    + specificIndividualName, response);
                }
            }
            else
                return SendErrorMessage(SC_FORBIDDEN, "need authentication : "
                        + AuthenticationRequiredToString(authenticationRequired), response);
        }
    }

    private boolean SendErrorMessage(int SC_STATUS, String message, HttpServletResponse response) throws IOException {
        response.setStatus(SC_STATUS);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error",message);
        IOUtil.writeMessage(response.getWriter(), jsonObject.toJSONString());

        return false;
    }

    private String AuthenticationRequiredToString(AuthenticationRequired authenticationRequired) {
        AuthenticationLevel[] authenticationLevels = authenticationRequired.levels();
        boolean[] specifics = authenticationRequired.specifics();

        if (authenticationLevels.length != specifics.length)
            throw new IllegalStateException("authenticationLevels and specifics length must match in Annotation AuthenticationRequired");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authenticationLevels.length; i++) {
            if (specifics[i])
                sb.append("specific ");
            sb.append(authenticationLevels[i].name);
            sb.append(";");
        }

        return sb.toString();
    }
}