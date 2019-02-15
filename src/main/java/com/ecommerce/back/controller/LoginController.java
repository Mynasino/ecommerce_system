package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.JWTInfo;
import com.ecommerce.back.model.User;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.PersonDetail;
import com.ecommerce.back.service.AdminService;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.MailUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import java.util.Date;

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
    public JWTInfo userLogin(@RequestParam("userName") String userName,
                            @RequestParam("password") String password) throws IllegalException {
        userService.loginUser(userName, password);
        //如果不抛出异常，则说明登陆成功，下发jwt并将用户名和过期时间放到全局ConcurrentHashMap(onlineUsers)中
        Date expiredTime = new Date();
        String jwtString = JWTUtil.getJWTString(userName, AuthenticationLevel.USER, expiredTime);
        Statistic.onlineUsers.put(userName, expiredTime);

        return new JWTInfo(JWTUtil.HEADER_KEY, jwtString);
    }

    @PostMapping("/admin")
    public JWTInfo adminLogin(@RequestParam("adminName") String adminName,
                             @RequestParam("password") String password) throws IllegalException {
        adminService.loginAdmin(adminName, password);
        //如果不抛出异常，则说明登陆成功，下发jwt
        return new JWTInfo(JWTUtil.HEADER_KEY, JWTUtil.getJWTString(adminName, AuthenticationLevel.ADMIN, new Date()));
    }

    @PatchMapping("/user/retrieve")
    public void sendPasswordResetMail(@RequestParam("userName") String individualName,
                                 @RequestParam("newPassword") String newPassword) throws IllegalException {
        User user = userService.getUserByUserName(individualName);
        if (user == null) throw new IllegalException("用户名", individualName, "不存在");
        String jwtString = JWTUtil.getJWTString(individualName, AuthenticationLevel.USER, new Date());
        try {
            MailUtil.sendMailMessage(user.getMail(), "Reset your password in " + websiteAddress,
                    "http://" + websiteAddress + "/login/user/retrieve" + "/" + jwtString + "/" + newPassword);
        } catch (MessagingException e) {
            throw new IllegalException("邮件地址", user.getMail(), "发送邮件失败");
        }
    }

    @GetMapping("/user/retrieve/{jwtString}/{newPassword}")
    public String retrieveURLVisit(@PathVariable("jwtString") String jwtString,
                                      @PathVariable("newPassword") String newPassword) {
        try {
            PersonDetail personDetail = JWTUtil.getPersonDetailByJWTString(jwtString);
            userService.modifyPassword(personDetail.getName(), newPassword);
            return "重置成功";
        } catch (ExpiredJwtException e) {
            return "Token过期，请重新申请";
        } catch (Exception e) {
            return "Token不合法";
        }
    }
}