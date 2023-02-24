package com.example.testdisasterevent.data.model;

public class AccountUserInfo {
    private String email;
    private String username;
    private String password;
    private String mobile;
    private String registerTime;
    private int userTypeID;
    private long uid;
    private String userType;

    public AccountUserInfo(String email, String username, String password, String mobile, String registerTime, int userTypeID, long uid, String userType) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.registerTime = registerTime;
        this.userTypeID = userTypeID;
        this.userType = userType;
        this.uid = uid;
    }
    public String getEmail() {return this.email;}
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getMobile() {return this.mobile;}
    public String getRegisterTime() {return this.registerTime;}
    public int getUserTypeID() {return this.userTypeID;}
    public long getUid() {return this.uid;}
    public String getUserType() {return this.userType;}
}
