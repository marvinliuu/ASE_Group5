package com.example.testdisasterevent.ui.login;


import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testdisasterevent.HomeActivity;
import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.ui.login.LoginViewModel;
import com.example.testdisasterevent.ui.login.LoginViewModelFactory;
import com.example.testdisasterevent.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private HideReturnsTransformationMethod method_show;
    private ActivityLoginBinding binding;
    private ImageView pwdVisible;
    private TextView tv_date;
    private TextView tv_content;
    private DatabaseReference mReference;
    private boolean pwdFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button backButton = binding.loginback;
        final ProgressBar loadingProgressBar = binding.loading;
        final ImageView logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(this).load(R.drawable.disaster_fire_logo).into(logo);
        tv_date = binding.tvDate;
        tv_content = binding.tvContent;
        loginButton.setEnabled(false);
        pwdVisible = binding.passwordVisible;



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        // input enter then go into this listen
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(home_intent);
            }
        });

        pwdVisible.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PasswordTransformationMethod methodHide=PasswordTransformationMethod.getInstance();
                passwordEditText.setTransformationMethod(methodHide);
                if(pwdFlag){
                    pwdVisible.setImageResource(R.drawable.eye1);
                    pwdFlag=false;
                    PasswordTransformationMethod method_hide = PasswordTransformationMethod.getInstance();
                    passwordEditText.setTransformationMethod(method_hide);
                }
                else{
                    pwdVisible.setImageResource(R.drawable.eye2);
                    pwdFlag=true;
                    method_show= HideReturnsTransformationMethod.getInstance();
                    passwordEditText.setTransformationMethod(method_show);
                }
            }
        });
    }

    private void initData() {
        //构建数据库实例对象，引用为mReference
        mReference = FirebaseDatabase.getInstance().getReference();
        //通过键名，获取数据库实例对象的子节点对象
        DatabaseReference date = mReference.child("date");
        DatabaseReference content = mReference.child("content");

        //注册子第一个节点对象数据变化的监听者对象
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //数据库数据变化时调用此方法
                String value = dataSnapshot.getValue(String.class);
                tv_date.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //注册子第二个节点对象数据变化的监听者对象
        content.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //数据库数据变化时调用此方法
                String value = dataSnapshot.getValue(String.class);
                tv_content.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main_intent);
    }

    private void showLoginFailed(Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}