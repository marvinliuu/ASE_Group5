package com.example.testdisasterevent.data;

import com.example.testdisasterevent.data.model.LoggedInUser;

public class RegisterRepository {

    private static volatile RegisterRepository instance;
    private RegisterDataSource dataSource;

    // private constructor : singleton access

    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }




    public Result<String> register(String username, String password, String email, String actCode) {
        // handle login
        Result<String> result = dataSource.register(username, password, email, actCode);
        return result;
    }


    public boolean isActCodeValid(String actCode) {
        // TODO: Search Database and find whether actCode VALID
        this.dataSource.setUserType(0);
        return true;
    }

    public boolean isEmailExist(String email) {
        //TODO: Check if this account already exist
        return true;
    }
}
