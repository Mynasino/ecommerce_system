package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "JWTInfo", description = "登陆成功后下发的JWT")
public class JWTInfo {
    @ApiModelProperty(value = "后续请求应该把token放在这个键值下")
    private String HeaderKey;
    @ApiModelProperty(value = "token字符串")
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
