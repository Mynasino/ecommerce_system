package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UsersInfo", description = "Information of specific user")
public class UserInfo {
    private int id;
    private String imgUrl;
    private String userName;
    private String password;

    public UserInfo(int id, String imgUrl, String userName, String password) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
