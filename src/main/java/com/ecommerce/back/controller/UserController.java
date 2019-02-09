package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.OnlineUsersInfo;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.ResponseUtil;
import io.swagger.annotations.*;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController //@Controller + @ResponseBody + return entity
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Register a user")
    @ApiResponses({
            @ApiResponse(code = SC_CREATED, message = "successful registration"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "fail to register", response = ErrorInfo.class)
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String register(@RequestBody RegisterInfo registerInfo, HttpServletResponse response) {
        String info = userService.registerUser(registerInfo);
        return info.equals("success") ?
                ResponseUtil.JSONResponse(SC_CREATED, "", response) :
                ResponseUtil.JSONResponse(SC_BAD_REQUEST, new ErrorInfo(info), response);
    }

    @ApiOperation(value = "Get Online Users")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getOnlineStatistic(HttpServletResponse response) {
        Set<String> keySet = Statistic.onlineUsers.keySet();
        return ResponseUtil.JSONResponse(SC_OK, new OnlineUsersInfo(keySet.size(), keySet), response);
    }
}