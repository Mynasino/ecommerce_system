package com.ecommerce.back.controller;

import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@RestController
public class AuthenticationTestController {
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("USER")
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {false})
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY) //required = true
    public String helloUSER() {
        return "hello USER";
    }

    @GetMapping("ADMIN")
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY) //required = true
    public String helloADMIN() {
        return "hello ADMIN";
    }

    @PostMapping(value = "specific USER")
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY)  //required = true
    public String helloSpecUSER(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) {
        return "hello specific USER " + individualName;
    }

    @PatchMapping(value = "specific ADMIN")
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {true})
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY)  //required = true
    public String helloSpecADMIN(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) {
        return "hello specific ADMIN " + individualName;
    }

    @PutMapping(value = "specific USER and all ADMIN")
    @AuthenticationRequired(levels = {AuthenticationLevel.USER, AuthenticationLevel.ADMIN}, specifics = {true,false})
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY) //required = true
    public String helloSpecUSERAndAllADMIN(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) {
        return "hello specific USER or ADMIN " + individualName;
    }
}
