package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.model.User;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UserUtil {
    public static void checkUserNameLegality(String userName) throws IllegalException {
        if (userName == null || userName.equals("")) throw new IllegalException("用户名",userName,"用户名不能为空");
        if (userName.length() > 10) throw new IllegalException("用户名",userName,"用户名长度不能超过10个字符");
    }

    public static void checkLegalPasswordLegality(String password) throws IllegalException {
        if (password == null || password.equals("")) throw new IllegalException("密码",password,"密码不能为空");
        if (password.length() > 10) throw new IllegalException("密码",password,"密码长度不能超过10个字符");
    }

    /**
     * 根据User实例里的明文密码password和盐salt，将同一个User实例的password字段编码为密文密码
     * @param user password为明文密码,salt为盐
     */
    public static void passwordEncode(User user) {
        String salt = user.getSalt();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));

        user.setPassword(password);
    }
}
