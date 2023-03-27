package com.hazelsoft.springsecurityjpa.model;

public class CrudUserRequest {

    private String userName;

    private Integer userId;

    public CrudUserRequest(String userName, Integer userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RemoveUserRequest{" +
                "userName='" + userName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
