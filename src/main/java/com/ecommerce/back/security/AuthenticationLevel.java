package com.ecommerce.back.security;

/**
 * 定义资源的访问权限
 */
public enum AuthenticationLevel {
    /** 需要USER的访问权限 **/
    USER("USER"),
    /** 需要ADMIN的访问权限 **/
    ADMIN("ADMIN");

    public final String name;
    AuthenticationLevel(String name) {
        this.name = name;
    }
}
