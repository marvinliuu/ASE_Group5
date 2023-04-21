package com.example.testdisasterevent.ui.login;


/**
 * Date: 23.01.28
 * Data validation state of the login form.
 * Version: Week 1
 */
class LoginFormState {
    private Integer usernameError;
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState( Integer usernameError, Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    Integer getUsernameError() {
        return usernameError;
    }

    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}