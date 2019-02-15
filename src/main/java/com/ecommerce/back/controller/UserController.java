package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.model.User;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController //@Controller + @ResponseBody + return entity
@RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void RegisterUser(@RequestBody RegisterInfo registerInfo) throws IllegalException, IOException {
        userService.registerUser(registerInfo);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping
    public void modifyPassWord(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("newPassword") String newPassword) throws IllegalException{
        userService.modifyPassword(individualName, newPassword);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping
    public User queryUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        return userService.getUserByUserName(individualName);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/productCollect")
    public void collectProduct(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("productId") int productId) throws IllegalException, DataAccessException {
        userService.collectProduct(individualName, productId);
    }



    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @DeleteMapping("/productCollect")
    public void cancelCollectProduct(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                 @RequestParam("productId") int productId) throws IllegalException {
        userService.cancelCollectProduct(individualName, productId);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER, AuthenticationLevel.ADMIN}, specifics = {true, false})
    @GetMapping("/productCollect")
    public List<Product> getProductCollection(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        return userService.getProductCollectionByUserName(individualName);
    }
}