package com.ecommerce.back.util;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.jsonInfo.ErrorInfo;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class ResponseUtil {
    public static String SC_OKorSC_BAD_REQUESTResponse(String info, HttpServletResponse response) {
        return info.equals("success") ?
                ResponseUtil.JSONResponse(SC_OK, "", response) :
                ResponseUtil.JSONResponse(SC_BAD_REQUEST, new ErrorInfo(info), response);
    }

    public static String JSONResponse(int SC_STATUS, Object message, HttpServletResponse response) {
        response.setStatus(SC_STATUS);
        return JSON.toJSONString(message);
    }
}
