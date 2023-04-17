package com.example.testdisasterevent.ui.register;


public class RegisterFormState {
    // Error message resource IDs for username, password, email, and activation code
    private Integer usernameError;
    private Integer passwordError;
    private Integer emailError;
    private Integer actCodeError;

    // Boolean to indicate whether the data in the form is valid
    private boolean isDataValid;

    // Constructor for an invalid form state with error messages
    RegisterFormState(Integer usernameError, Integer passwordError, Integer emailError, Integer actCodeError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.emailError = emailError;
        this.actCodeError = actCodeError;
        this.isDataValid = false;
    }

    // Constructor for a valid form state without error messages
    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.emailError = null;
        this.actCodeError = null;
        this.isDataValid = isDataValid;
    }

    // Getter methods for error message resource IDs
    Integer getUsernameError() {
        return usernameError;
    }
    Integer getPasswordError() {
        return passwordError;
    }
    Integer getEmailError() {
        return emailError;
    }
    Integer getActCodeError() {
        return actCodeError;
    }

    // Getter method for the isDataValid boolean
    boolean isDataValid() {
        return isDataValid;
    }
}

