package com.ecommerce.back.controller;

import com.ecommerce.back.jsonInfo.ErrorInfo;
import com.ecommerce.back.jsonInfo.JWTInfo;
import com.ecommerce.back.model.User;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.PersonDetail;
import com.ecommerce.back.service.AdminService;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.MailUtil;
import com.ecommerce.back.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    private AdminService adminService;

    @Value("${website.address}")
    private String websiteAddress;

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
        if (info.equals("success")) {
            Date expiredTime = new Date();
            String jwtString = JWTUtil.getJWTString(userName, AuthenticationLevel.USER, expiredTime);
            Statistic.onlineUsers.put(userName, expiredTime);

            return ResponseUtil.JSONResponse(SC_OK, new JWTInfo(JWTUtil.HEADER_KEY, jwtString), response);
        }
        else
            return ResponseUtil.JSONResponse(SC_BAD_REQUEST,
                    new ErrorInfo(info), response);
    }

    @PostMapping("/admin")
    public String adminLogin(@RequestParam("adminName") String adminName,
                             @RequestParam("password") String password,
                             HttpServletResponse response) {
        String info = adminService.loginAdmin(adminName, password);
        return info.equals("success") ?
                ResponseUtil.JSONResponse(SC_OK,
                        new JWTInfo(JWTUtil.HEADER_KEY, JWTUtil.getJWTString(adminName, AuthenticationLevel.ADMIN, new Date())), response) :
                ResponseUtil.JSONResponse(SC_BAD_REQUEST, new ErrorInfo(info), response);
    }

    @PatchMapping("/user/retrieve")
    public void sendPasswordResetMail(@RequestParam("userName") String individualName,
                                 @RequestParam("newPassword") String newPassword) {
        User user = userService.getUserByUserName(individualName);
        if (user == null) throw new IllegalStateException("user not exist");
        String jwtString = JWTUtil.getJWTString(individualName, AuthenticationLevel.USER, new Date());
        try {
            MailUtil.sendMailMessage(user.getMail(), "Reset your password in " + websiteAddress,
                    "http://" + websiteAddress + "/login/user/retrieve" + "/" + jwtString + "/" + newPassword);
        } catch (MessagingException e) {
            throw new IllegalStateException("Mail send failed");
        }
    }

    @GetMapping("/user/retrieve/{jwtString}/{newPassword}")
    public String retrieveURLVisit(@PathVariable("jwtString") String jwtString,
                                      @PathVariable("newPassword") String newPassword) {
        try {
            PersonDetail personDetail = JWTUtil.getPersonDetailByJWTString(jwtString);
            userService.modifyPassword(personDetail.getName(), newPassword);
            return "modify password success";
        } catch (ExpiredJwtException e) {
            return "JWT Expired, please relogin";
        } catch (Exception e) {
            return "JWT illegal";
        }
    }
}