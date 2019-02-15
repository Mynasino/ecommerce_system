package com.ecommerce.back.exception;

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
