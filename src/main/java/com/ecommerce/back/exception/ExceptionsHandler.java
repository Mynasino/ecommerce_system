package com.ecommerce.back.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecommerce.back.security.exception.JWTExpiredException;
import com.ecommerce.back.security.exception.JWTIllegalException;
import com.ecommerce.back.security.exception.LoginFirstException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 用于统一捕获请求过程的异常
 * 并返回格式化的信息
 */
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(IllegalException.class)
    @ResponseBody
    public String IllegalExceptionHandler(IllegalException e, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", e.getName());
        jsonObject.put("value", e.getValue());
        jsonObject.put("message", e.getMessage());

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return jsonObject.toJSONString();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public String UnauthorizedExceptionHandler(UnauthorizedException e, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tryVisitUser", e.getTryVisitUser());
        jsonObject.put("ownerUser", e.getOwnerUser());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        return jsonObject.toJSONString();
    }

    @ExceptionHandler({LoginFirstException.class, JWTExpiredException.class, JWTIllegalException.class})
    @ResponseBody
    public String LoginFirstExceptionHandler(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", e.getMessage());
        return jsonObject.toJSONString();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public String SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", e.getMessage());
        return jsonObject.toJSONString();
    }
}
