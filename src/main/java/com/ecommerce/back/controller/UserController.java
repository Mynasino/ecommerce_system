package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;

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
        if (info.equals("success")) {
            response.setStatus(SC_CREATED);
            return "";
        }
        else {
            response.setStatus(SC_BAD_REQUEST);
            return JSON.toJSONString(new ErrorInfo(info));
        }
    }
}