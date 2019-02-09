package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "JWTInfo", description = "JWT info after successful login")
public class JWTInfo {
    @ApiModelProperty(value = "Key of JWT in Header")
    private String HeaderKey;
    @ApiModelProperty(value = "JWT String")
    private String jwtString;

    public JWTInfo(String headerKey, String jwtString) {
        HeaderKey = headerKey;
        this.jwtString = jwtString;
    }

    public String getHeaderKey() {
        return HeaderKey;
    }

    public void setHeaderKey(String headerKey) {
        HeaderKey = headerKey;
    }

    public String getJwtString() {
        return jwtString;
    }

    public void setJwtString(String jwtString) {
        this.jwtString = jwtString;
    }
}
