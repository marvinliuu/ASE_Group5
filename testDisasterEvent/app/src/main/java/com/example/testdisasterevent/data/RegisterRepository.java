package com.example.testdisasterevent.data;

import com.example.testdisasterevent.data.model.LoggedInUser;

public class RegisterRepository {

    private static volatile RegisterRepository instance;
    private RegisterDataSource dataSource;


    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    /**
     * Date: 23.04.14
     * Function: execute the register process
     * Author: Siyu Liao
     * Version: Week 12
     */
    public Result<String> register(String username, String password, String email, String phone, String actCode) {
        // handle login
        Result<String> result = dataSource.register(username, password, email, phone, actCode);
        return result;
    }

    public boolean isActCodeValid(String actCode) {
        this.dataSource.setUserType(0);
        return true;
    }
}
