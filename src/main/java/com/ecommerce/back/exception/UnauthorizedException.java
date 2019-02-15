package com.ecommerce.back.exception;

/**
 * 用户已登陆，但请求的资源权限属于另一个用户
 */
public class UnauthorizedException extends Exception {
    private String tryVisitUser;
    private String ownerUser;

    public UnauthorizedException(String tryVisitUser, String ownerUser) {
        super();
        this.tryVisitUser = tryVisitUser;
        this.ownerUser = ownerUser;
    }

    public String getTryVisitUser() {
        return tryVisitUser;
    }

    public String getOwnerUser() {
        return ownerUser;
    }
}
