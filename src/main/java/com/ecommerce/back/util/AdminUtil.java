package com.ecommerce.back.util;

import com.ecommerce.back.model.Admin;
import com.ecommerce.back.model.User;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class AdminUtil {
    /**
     * 用传入的Admin实例的password和salt来编码password
     * @param admin 要将密码转为密文的Admin实例
     */
    public static void passwordEncode(Admin admin) {
        String salt = admin.getSalt();
        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));

        admin.setPassword(password);
    }
}
