package com.example.testdisasterevent.ui.login;

import androidx.annotation.Nullable;

/**
 * Date: 23.01.28
 * Function: Authentication result - success (user details) or error message of Login Process.
 * Author: Siyu Liao
 * Version: Week 1
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}