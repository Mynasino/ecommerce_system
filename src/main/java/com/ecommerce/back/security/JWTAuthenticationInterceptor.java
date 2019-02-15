package com.ecommerce.back.security;

import com.ecommerce.back.security.exception.JWTExpiredException;
import com.ecommerce.back.security.exception.JWTIllegalException;
import com.ecommerce.back.security.exception.LoginFirstException;
import com.ecommerce.back.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 保护Controller方法调用的拦截器
 * 从请求头取出Token并验证是否具有对应权限
 */
@Component
public class JWTAuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
            throws LoginFirstException, JWTExpiredException, JWTIllegalException {
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
            if (jwtString == null) throw new LoginFirstException("请先登陆");
            //try parse JWT
            PersonDetail personDetail;
            try {
                personDetail = JWTUtil.getPersonDetailByJWTString(jwtString);
            } catch (ExpiredJwtException e) {
                throw new JWTExpiredException("登陆已过期，请重新登陆");
            } catch (Exception e) {
                throw new JWTIllegalException("登陆Token不合法，请重新登陆");
            }
            //jwt parse fail
            if (personDetail == null) throw new JWTIllegalException("登陆Token不合法，请重新登陆");
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
                    if (specificIndividualName == null)
                        throw new IllegalArgumentException("请求错误，需要带上RequestParam " + JWTUtil.SPECIFIC_PARAM_NAME);
                    if (!personDetail.getName().equals(specificIndividualName))
                        throw new LoginFirstException("权限不足，需要如下权限:" + AuthenticationRequiredToString(authenticationRequired) + "，请重新登陆");
                }
            }
            else
                throw new LoginFirstException("权限不足，需要如下权限:" + AuthenticationRequiredToString(authenticationRequired) + "，请重新登陆");
        }

        return true;
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