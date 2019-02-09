package com.ecommerce.back.service;

import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.model.User;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.UserUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class UserService {
    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String registerUser(RegisterInfo registerInfo) {
        String userName = registerInfo.getUserName();
        String password = registerInfo.getPassword();

        if ((!UserUtil.isLegalUserName(userName)) || (!UserUtil.isLegalPassword(password)))
            return "userName/password is not legal";
        if (userDAO.getUserByUserName(userName) != null)
            return "userName already exist";

        byte[] base64ImgBytes = registerInfo.getImgBase64String().getBytes(StandardCharsets.UTF_8);
        try {
            String newImgUrl = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, registerInfo.getImgType());
            //encode and add user
            User user = new User(userName, password, newImgUrl, UUID.randomUUID().toString().substring(0,5));
            UserUtil.passwordEncode(user);
            userDAO.addUser(user);

            return "success";
        }  catch (IllegalStateException e) {
            e.printStackTrace();
            return "imgType not support";
        } catch (IOException e) {
            e.printStackTrace();
            return "server IOException";
        }
    }

    public String loginUser(String userName, String password) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null)
            return "no user with this user name";
        else {
            User attemptUser = new User(userName, password, user.getImgUrl(), user.getSalt());
            UserUtil.passwordEncode(attemptUser);
            return attemptUser.equals(user) ? "success" : "password not correct";
        }
    }

    public String modifyPassword(String userName, String password) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null)
            return "no user with this user name";
        else {
            User userWithNewPwd = new User(userName, password, user.getImgUrl(), user.getSalt());
            UserUtil.passwordEncode(userWithNewPwd);
            return (userDAO.updateUserPassword(userWithNewPwd) != 0) ? "success" : "update fail";
        }
    }
}
