package com.ecommerce.back.security;

public class PersonDetail {
    private String name;
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
