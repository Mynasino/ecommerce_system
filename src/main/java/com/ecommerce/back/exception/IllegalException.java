package com.ecommerce.back.exception;

public class IllegalException extends Exception {
    private String name;
    private String value;
    private String message;

    public IllegalException(String name, String value, String message) {
        this.name = name;
        this.value = value;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
