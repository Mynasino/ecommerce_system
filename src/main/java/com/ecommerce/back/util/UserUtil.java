package com.ecommerce.back.util;

import com.ecommerce.back.model.User;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UserUtil {
    public static boolean isLegalUserName(String userName) {
        return userName != null && !userName.equals("");
    }
    public static boolean isLegalPassword(String password) {
        return password != null && !password.equals("");
    }
    public static void passwordEncode(User user) {
        String salt = user.getSalt();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));

        user.setPassword(password);
    }
}
