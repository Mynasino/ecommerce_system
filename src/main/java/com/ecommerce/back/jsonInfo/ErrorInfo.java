package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ErrorInfo", description = "A description of error")
public class ErrorInfo {
    @ApiModelProperty(value = "error description")
    private String error;

    public ErrorInfo(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
