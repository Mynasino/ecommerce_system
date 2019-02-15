package com.ecommerce.back.security;

/**
 * 对User和Admin类的统一封装
 * 用于解析请求头中的JWT后返回统一的实体类
 */
public class PersonDetail {
    /**
     * 用户名或管理员名
     */
    private String name;
    /**
     * 具备的权限级别
     */
    private AuthenticationLevel authenticationLevel;

    public PersonDetail(String name, AuthenticationLevel authenticationLevel) {
        this.name = name;
        this.authenticationLevel = authenticationLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    AuthenticationLevel getAuthenticationLevel() {
        return authenticationLevel;
    }

    void setAuthenticationLevel(AuthenticationLevel authenticationLevel) {
        this.authenticationLevel = authenticationLevel;
    }
}
