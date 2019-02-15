package com.ecommerce.back.security.exception;

public class JWTExpiredException extends Exception {
    public JWTExpiredException(String message) {
        super(message);
    }
}
