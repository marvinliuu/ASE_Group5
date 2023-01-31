package com.example.testdisasterevent.data;

import com.example.testdisasterevent.data.model.LoggedInUser;

import java.io.IOException;

public class RegisterDataSource {
    private int userType;

    public Result<String>  register(String username, String password, String email, String actCode) {

        try {
            // TODO: insert RegisterInfo (Insert to Table) Database

            return new Result.Success<>("Register Success" + username);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register", e));
        }
    }

    public void setUserType(int userType) {
        // TODO: check if type valid
        this.userType = userType;
    }
}
