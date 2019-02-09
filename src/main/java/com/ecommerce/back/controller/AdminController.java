package com.ecommerce.back.controller;

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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public  AdminController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get Online Users")
    @GetMapping(value = "/onlineUsers", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getOnlineStatistic(HttpServletResponse response) {
        Set<String> keySet = Statistic.onlineUsers.keySet();
        return ResponseUtil.JSONResponse(SC_OK, new OnlineUsersInfo(keySet.size(), keySet), response);
    }

    @ApiOperation(value = "Query a user with user himself or ADMIN authority")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER, AuthenticationLevel.ADMIN}, specifics = {true, false})
    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String queryUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                            HttpServletResponse response) {
        User user = userService.getUserByUserName(individualName);
        return (user == null) ?
                ResponseUtil.SC_OKorSC_BAD_REQUESTResponse("no user with user name " + individualName, response) :
                ResponseUtil.JSONResponse(SC_OK, new UserInfo(user.getId(), user.getImgUrl(), user.getUserName(), user.getPassword()), response);
    }

    @ApiOperation(value = "modify password")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PatchMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String modifyUser(@RequestBody RegisterInfo registerInfo,
                             @RequestParam("userId") int id,
                             HttpServletResponse response) {
        String info = userService.updateUser(registerInfo, id);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiOperation(value = "Delete a user with ADMIN authority")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String deleteUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                             HttpServletResponse response) {
        String info = userService.deleteUser(individualName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }
}
