package com.ecommerce.back.controller;

import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.JWTInfo;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.service.AdminService;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    private AdminService adminService;

    @Autowired
    public LoginController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/user")
    public String userLogin(@RequestParam("userName") String userName,
                            @RequestParam("password") String password,
                            HttpServletResponse response) {
        String info = userService.loginUser(userName, password);
        if (info.equals("success"))
            return ResponseUtil.JSONResponse(SC_OK,
                    new JWTInfo(JWTUtil.HEADER_KEY, JWTUtil.getJWTString(userName, AuthenticationLevel.USER)), response);
        else
            return ResponseUtil.JSONResponse(SC_BAD_REQUEST,
                    new ErrorInfo(info), response);
    }

    @PostMapping("/admin")
    public String adminLogin(@RequestParam("adminName") String adminName,
                             @RequestParam("password") String password,
                             HttpServletResponse response) {
        String info = adminService.loginAdmin(adminName, password);
        if (info.equals("success"))
            return ResponseUtil.JSONResponse(SC_OK,
                    new JWTInfo(JWTUtil.HEADER_KEY, JWTUtil.getJWTString(adminName, AuthenticationLevel.ADMIN)), response);
        else
            return ResponseUtil.JSONResponse(SC_BAD_REQUEST,
                    new ErrorInfo(info), response);
    }
}