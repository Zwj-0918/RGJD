package com.example.app1.data;

import com.example.app1.data.model.LoggedInUser;
import com.example.app1.data.model.RegisterUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegisterDataSource {

    public Result<RegisterUser> register(String username, String phone, String password, String repassword) {

        try {
            // TODO: handle loggedInUser authentication
            RegisterUser fakeUser =
                    new RegisterUser(
                            java.util.UUID.randomUUID().toString(),
                            "我们的小英雄！");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}