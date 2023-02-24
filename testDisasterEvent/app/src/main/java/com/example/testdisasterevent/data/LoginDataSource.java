package com.example.testdisasterevent.data;

import com.example.testdisasterevent.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(int loginStatus, String displayName, String loginUserID) {

        try {
            // TODO: handle loggedInUser authentication
            if(loginStatus == 2){
                LoggedInUser loggedUser =
                        new LoggedInUser(
                                loginUserID,
                                "Welcome! " + displayName);
                return new Result.Success<>(loggedUser);
            }
            else if(loginStatus == 1){
                return new Result.Failure("Wrong username or wrong password.");
            }
            else{
                return new Result.Failure("User doesn't exist.");
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}