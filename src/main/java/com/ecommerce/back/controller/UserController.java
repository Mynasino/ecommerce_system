package com.ecommerce.back.controller;

import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.OnlineUsersInfo;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.jsonInfo.UserInfo;
import com.ecommerce.back.model.Product;
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

import java.util.List;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController //@Controller + @ResponseBody + return entity
@RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String RegisterUser(@RequestBody RegisterInfo registerInfo, HttpServletResponse response) {
        String info = userService.registerUser(registerInfo);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping
    public String modifyPassWord(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpServletResponse response) {
        String info = userService.modifyPassword(individualName, newPassword);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/productCollect")
    public String collectProduct(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("productId") int productId,
                                 HttpServletResponse response) {
        String info = userService.collectProduct(individualName, productId);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @DeleteMapping("/productCollect")
    public String cancelCollectProduct(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("productId") int productId,
                                 HttpServletResponse response) {
        String info = userService.cancelCollectProduct(individualName, productId);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/productCollect")
    public List<Product> getProductCollection(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                              HttpServletResponse response) {
        return userService.getProductCollectionByUserName(individualName);
    }
}