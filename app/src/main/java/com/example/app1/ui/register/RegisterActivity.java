package com.example.app1.ui.register;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.app1.DB.DatabaseHelper;
import com.example.app1.DB.UserInfo;
import com.example.app1.R;
import com.example.app1.databinding.ActivityRegisterBinding;
import com.example.app1.dbConnect;
import com.example.app1.ui.login.LoginActivity;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0){
                Toast.makeText(RegisterActivity.this,"异常！注册失败！",Toast.LENGTH_SHORT).show();
            }
            else if (msg.what == 1){
                Toast.makeText(RegisterActivity.this,"该手机号已经注册过！",Toast.LENGTH_SHORT).show();
            }else if(msg.what ==2){

            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);
//        将版本号更改为比一大的数可以调用upGrade方法
        dbHelper = new DatabaseHelper(this,"UserInfo.dp",null,2);



        final EditText usernameEditText = binding.username;
        final EditText phoneEditText = binding.phone;
        final EditText passwordEditText = binding.password;
        final EditText repasswordEditText= binding.repassword;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView btn_login = binding.toLogin;

//        String username=usernameEditText.getText().toString();
//        String phone=phoneEditText.getText().toString();
//        String password=passwordEditText.getText().toString();
//        String repassword=repasswordEditText.getText().toString();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if(registerFormState.getPhoneError()!=null){
                    phoneEditText.setError(getString(registerFormState.getPhoneError()));
                }
                if(registerFormState.getRepasswordError()!=null){
                    repasswordEditText.setError(getString(registerFormState.getRepasswordError()));
                }
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess(),usernameEditText.getText().toString(),phoneEditText.getText().toString(),passwordEditText.getText().toString());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(usernameEditText.getText().toString(),phoneEditText.getText().toString(),
                        passwordEditText.getText().toString(),repasswordEditText.getText().toString());
            }
        };
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        repasswordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(usernameEditText.getText().toString(),phoneEditText.getText().toString(),
                            passwordEditText.getText().toString(),repasswordEditText.getText().toString());
                }
                return false;
            }
        });
        repasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(usernameEditText.getText().toString(),phoneEditText.getText().toString(),
                            passwordEditText.getText().toString(),repasswordEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        从数据库中查询手机号是否注册过
                String phone=phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                boolean res = registerUser(phone,password,username);

                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(usernameEditText.getText().toString(),phoneEditText.getText().toString(),
                        passwordEditText.getText().toString(),repasswordEditText.getText().toString());
                Log.d("register","res:"+ res);
                if(res==false)
                    Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
    public Boolean registerUser(String phone,String password,String username){
        UserInfo userInfo = new UserInfo(phone, password, username);
        Message msg = new Message();
        new Thread(){
            @Override
            public void run(){
                try {
                    Message msg = new Message();
                    HashMap<String,String> mp = dbConnect.getUserInfoByPhone(phone);
                    Log.d("Register",mp+ "");
                    Log.d("Register","mp:"+mp.size());
                    if(mp.size()!=0){
                        msg.what = 1;
                    }else{
                        if(dbConnect.addUserInfo(userInfo)){
                            msg.what = 2;
                        }
                    }
                    handler.sendMessage(msg);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
        if(msg.what == 2)
            return true;
        else
            return false;
    }

    private void updateUiWithUser(RegisterUserView model,String username,String phone,String password) {

//        String welcome = getString(R.string.welcome) + model.getDisplayName();
        String welcome = getString(R.string.register_success);

        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();


    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}