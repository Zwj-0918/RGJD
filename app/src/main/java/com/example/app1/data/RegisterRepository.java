package com.example.app1.data;

import com.example.app1.data.model.LoggedInUser;
import com.example.app1.data.model.RegisterUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private RegisterUser user = null;

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

    public boolean isRegister() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setRegisterUser(RegisterUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<RegisterUser> register(String username, String phone, String password, String repassword) {
        // handle login
        Result<RegisterUser> result = dataSource.register(username,phone,password,repassword);
        if (result instanceof Result.Success) {
            setRegisterUser(((Result.Success<RegisterUser>) result).getData());
        }
        return result;
    }

}