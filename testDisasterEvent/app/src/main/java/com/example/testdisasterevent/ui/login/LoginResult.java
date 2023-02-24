package com.example.testdisasterevent.ui.login;


/**
 * Date: 23.01.28
 * Function: Authentication result - success (user details) or error message of Login Process.
 * Author: Siyu Liao
 * Version: Week 1
 */
class LoginResult {
    private LoggedInUserView success;
    private Integer error;
    private String failure;

    LoginResult( String failure) { this.failure = failure; }

    LoginResult( Integer error) { this.error = error; }

    LoginResult( LoggedInUserView success) {
        this.success = success;
    }

    LoggedInUserView getSuccess() {
        return success;
    }

    Integer getError() {
        return error;
    }

    String  getWrong() { return failure; }
}