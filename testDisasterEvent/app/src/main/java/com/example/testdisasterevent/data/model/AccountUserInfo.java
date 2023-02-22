package com.example.testdisasterevent.data.model;

public class AccountUserInfo {
    private String username;
    private String email;
    private String userType;
    private String mobile;

    public AccountUserInfo(String username, String email, String mobile, String userType) {
        this.username = username;
        this.userType = userType;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUsername() {return this.username;}
    public String getEmail() {return this.email;}
    public String getMobile() {return this.mobile;}
    public String getUserType() {return this.userType;}
}
