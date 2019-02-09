package com.ecommerce.back.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
    public static String JSONResponse(int SC_STATUS, Object message, HttpServletResponse response) {
        response.setStatus(SC_STATUS);
        return JSON.toJSONString(message);
    }
}
