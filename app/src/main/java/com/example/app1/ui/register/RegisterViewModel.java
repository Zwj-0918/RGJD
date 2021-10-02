package com.example.app1.ui.register;


import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app1.R;
import com.example.app1.data.RegisterRepository;
import com.example.app1.data.Result;
import com.example.app1.data.model.RegisterUser;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;





    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String phone, String password,String repassword) {
        // can be launched in a separate asynchronous job
        Result<RegisterUser> result = registerRepository.register(username, phone, password, repassword);

        if (result instanceof Result.Success) {
            RegisterUser data = ((Result.Success<RegisterUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisterUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.register_failed));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registerDataChanged(String username, String phone, String password, String repassword) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username,null,null,null));
        }else if(!isPhoneValid(phone)){
            registerFormState.setValue(new RegisterFormState(null,R.string.invalid_phone,null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null,null, R.string.invalid_password,null));
        } else if (!isRepasswordCorrect(password, repassword)){
            registerFormState.setValue(new RegisterFormState(null,null,null,R.string.incorrect_repassword));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
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
    private boolean isRepasswordCorrect(String password,String repassword){
        return password.equals(repassword);
    }
    private boolean isPhoneValid(String phone){
        //检验电话是否正确
        String telRegex = "[1][358]\\d{9}";
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)){
            return false;
        }
        else return phone.matches(telRegex);
    }

}