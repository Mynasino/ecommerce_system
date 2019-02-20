package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.OnlineUsersInfo;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.model.User;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.UserService;
import com.ecommerce.back.statistic.Statistic;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class AdminController {
    private UserService userService;

    @Autowired
    public  AdminController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("获取在线用户数量和在线用户的用户名数组")
    @GetMapping(value = "/onlineUsers")
    public OnlineUsersInfo getOnlineStatistic() {
        Set<String> keySet = Statistic.onlineUsers.keySet();
        return new OnlineUsersInfo(keySet.size(), keySet);
    }

    @ApiOperation("获取所有用户，需要在header放管理员token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @GetMapping(value = "/users")
    public List<User> getUsersByLimitAndOffset(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
        return userService.getUsersByLimitAndOffset(limit, offset);
    }

    @ApiOperation("查询用户名为individualName的用户的信息，需要在header放管理员token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @GetMapping(value = "/user")
    public User queryUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        return userService.getUserByUserName(individualName);
    }

    @ApiOperation("修改用户Id为id的用户，RegisterInfo里放修改后的用户信息，需要在header放管理员token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PutMapping(value = "/user")
    public void modifyUser(@RequestBody RegisterInfo registerInfo,
                             @RequestParam("userId") int id) throws IllegalException, IOException {
        userService.updateUser(registerInfo, id);
    }

    @ApiOperation("删除用户名为individualName的用户，需要在header放管理员token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping(value = "/user")
    public void deleteUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) {
        userService.deleteUser(individualName);
    }
}
