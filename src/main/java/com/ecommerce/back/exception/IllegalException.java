package com.ecommerce.back.exception;

/**
 * 提交的某个信息不合法
 */
public class IllegalException extends Exception {
    /**
     * 不合法信息名
     */
    private String name;
    /**
     * 不合法信息取值
     */
    private String value;
    /**
     * 原因
     */
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
