package com.example.testdisasterevent.ui.login;

/**
 * Date: 23.01.28
 * Class exposing authenticated user details to the UI.
 * Author: Siyu Liao
 * Version: Week 1
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}