package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel(value = "OnlineUsersInfo", description = "A description of users online")
public class OnlineUsersInfo {
    @ApiModelProperty(value = "the number of users online")
    private int userCount;
    @ApiModelProperty(value = "the names of users online")
    private Set<String> userNames;

    public OnlineUsersInfo(int userCount, Set<String> userNames) {
        this.userCount = userCount;
        this.userNames = userNames;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }
}
