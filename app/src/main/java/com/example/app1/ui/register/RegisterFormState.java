package com.example.app1.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    private Integer phoneError;
    @Nullable
    private Integer passwordError;
    private Integer repasswordError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer phoneError, @Nullable Integer passwordError, @Nullable Integer repasswordError) {
        this.usernameError = usernameError;
        this.phoneError = phoneError;
        this.passwordError = passwordError;
        this.repasswordError = repasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.phoneError = null;
        this.usernameError = null;
        this.passwordError = null;
        this.repasswordError = null;
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

    @Nullable
    Integer getPhoneError(){return phoneError;}

    @Nullable
    Integer getRepasswordError(){return repasswordError;}

    boolean isDataValid() {
        return isDataValid;
    }
}