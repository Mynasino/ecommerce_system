package com.ecommerce.back.security;

/**
 * Defines the authentication level for resources
 */
public enum AuthenticationLevel {
    /** required USER Authentication **/
    USER("USER"),
    /** required ADMIN Authentication **/
    ADMIN("ADMIN");

    public final String name;
    AuthenticationLevel(String name) {
        this.name = name;
    }
}
