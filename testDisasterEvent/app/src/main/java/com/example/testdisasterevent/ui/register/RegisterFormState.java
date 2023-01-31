package com.example.testdisasterevent.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private Integer emailError;
    private Integer actCodeError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer emailError, @Nullable Integer actCodeError) {
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

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }
    Integer getEmailError() { return emailError; }
    Integer getActCodeError() { return actCodeError; }
    boolean isDataValid() {
        return isDataValid;
    }
}
