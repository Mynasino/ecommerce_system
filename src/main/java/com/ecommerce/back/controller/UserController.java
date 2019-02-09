package com.ecommerce.back.controller;

import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.OnlineUsersInfo;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.jsonInfo.UserInfo;
import com.ecommerce.back.model.User;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.ResponseUtil;
import com.sun.deploy.net.HttpResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.Set;

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
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String register(@RequestBody RegisterInfo registerInfo, HttpServletResponse response) {
        String info = userService.registerUser(registerInfo);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiOperation(value = "modify password")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String modifyPassWord(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpServletResponse response) {
        String info = userService.modifyPassword(individualName, newPassword);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }
}