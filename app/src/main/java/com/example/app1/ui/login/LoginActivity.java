package com.example.app1.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.DatabaseHelper;
import com.example.app1.MainActivity;
import com.example.app1.R;
import com.example.app1.UserInfo;
import com.example.app1.ui.login.LoginViewModel;
import com.example.app1.ui.login.LoginViewModelFactory;
import com.example.app1.databinding.ActivityLoginBinding;
import com.example.app1.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
//    MODE_PRIVATE说明只有本应用可以使用
    SharedPreferences mshared;
    SharedPreferences.Editor mEditor;

    private DatabaseHelper dbHelper;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private CheckBox btn_rem;
    private TextView tv_toRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(this,"UserInfo.dp",null,2);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        mshared=getSharedPreferences("remPwd", Context.MODE_PRIVATE);
        mEditor= mshared.edit();

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;





        if(usernameEditText.getText().toString()!=null&&passwordEditText.getText().toString()!=null)
            loginViewModel.loginDataChanged(passwordEditText.getText().toString(),usernameEditText.getText().toString());

        btn_rem=findViewById(R.id.rb_rem);
        btn_rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        tv_toRegister=findViewById(R.id.to_register);
        tv_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,com.example.app1.ui.register.RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getPhoneError() != null) {
                    usernameEditText.setError(getString(loginFormState.getPhoneError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
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

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(passwordEditText.getText().toString(),usernameEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(passwordEditText.getText().toString(),usernameEditText.getText().toString());
                }
                return false;
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String username=mshared.getString("username","");
                String passward=mshared.getString("passward","");
                String usernameEdit=usernameEditText.getText().toString();
                if(username.equals(usernameEdit))
                    passwordEditText.setText(passward);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=usernameEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                //检验账号密码是否对应，还未完成

                Cursor cursor = db.query("UserInfo",new String[]{"phone","password"},"phone=?",new String[]{phone},null,null,"password");
                if(cursor.getCount()<=0){
                    Toast.makeText(LoginActivity.this,"该手机号未注册过，请先注册！",Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                while(cursor.moveToNext()) {
                    @SuppressLint("Range") String pwd = cursor.getString(cursor.getColumnIndex("password"));
                    if (!pwd.equals(password)) {
                        Toast.makeText(LoginActivity.this, "账号密码有误！", Toast.LENGTH_SHORT).show();
                        cursor.close();
                        return;
                    }
                }

                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(passwordEditText.getText().toString(),usernameEditText.getText().toString());

                if(btn_rem.isChecked()==true){
                    //记住密码将密码上传到sharedpreferences
                    mEditor.putString("username",usernameEditText.getText().toString());
                    mEditor.putString("passward",passwordEditText.getText().toString());
                    mEditor.commit();
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent=new Intent(LoginActivity.this, com.example.app1.MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}