package com.example.testdisasterevent.ui.register;


public class RegisterFormState {
    private Integer usernameError;
    private Integer passwordError;
    private Integer emailError;
    private Integer actCodeError;
    private boolean isDataValid;

    RegisterFormState( Integer usernameError,  Integer passwordError,  Integer emailError,  Integer actCodeError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.emailError = emailError;
        this.actCodeError = actCodeError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.emailError = null;
        this.actCodeError = null;
        this.isDataValid = isDataValid;
    }

    Integer getUsernameError() {
        return usernameError;
    }

    Integer getPasswordError() {
        return passwordError;
    }
    Integer getEmailError() { return emailError; }
    Integer getActCodeError() { return actCodeError; }
    boolean isDataValid() {
        return isDataValid;
    }
}
