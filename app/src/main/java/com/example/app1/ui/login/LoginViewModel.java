package com.example.app1.ui.login;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.app1.data.LoginRepository;
import com.example.app1.data.Result;
import com.example.app1.data.model.LoggedInUser;
import com.example.app1.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String password,String phone) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(password,phone);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String password, String phone) {
        if (!isPhoneValid(phone)) {
            loginFormState.setValue(new LoginFormState(null,R.string.invalid_phone));
        }else if(!isPasswordValid(password)){
            loginFormState.setValue(new LoginFormState( R.string.invalid_password,null));
        }else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder phone validation check
    private boolean isPhoneValid(String phone){
        //????????????????????????
        String telRegex = "[1][358]\\d{9}";
        //"[1]"?????????1????????????1???"[358]"????????????????????????3???5???8???????????????"\\d{9}"????????????????????????0???9???????????????9??????
        if (TextUtils.isEmpty(phone)){
            return false;
        }
        else return phone.matches(telRegex);
    }
}