package com.example.testdisasterevent.ui.register;


public class RegisterResult {
    private Integer error;
    private String failure;
    private String status;
    RegisterResult( String status) { this.status = status; }
    String getStatus() { return status; }

}
