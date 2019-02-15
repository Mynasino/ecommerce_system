package com.ecommerce.back.security.exception;

public class JWTIllegalException extends Exception {
    public JWTIllegalException(String message) {
        super(message);
    }
}
