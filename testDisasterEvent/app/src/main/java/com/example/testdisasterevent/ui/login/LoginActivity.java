package com.example.testdisasterevent.ui.login;


import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testdisasterevent.HomeActivity;
import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.algorithms.ResearchAllocation;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.databinding.ActivityLoginBinding;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {

    private String accountUserInfoJson;
    private LoginViewModel loginViewModel;
    private HideReturnsTransformationMethod method_show;
    private ActivityLoginBinding binding;
    private ImageView pwdVisible, logo;
    private boolean pwdFlag = false;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, backButton;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create new object of the view model
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // bind the certain activity xml
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get the certain widget in the activity xml
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        backButton = binding.loginback;
        loadingProgressBar = binding.loading;
        // set image of the imageview
        logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(this).load(R.drawable.disaster_fire_logo).into(logo);
        pwdVisible = binding.passwordVisible;


        loginButton.setEnabled(false);

        // set all the observer
        setDataObserver();
        setListeners();
    }

    /**
     * Date: 23.04.12
     * Function: set all the viewmodel data observer
     * Version: Week 12
     */
    private void setDataObserver() {
        /**
         * Observer
         * Function: observe the LoginFormState class data change signal
         */
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged( LoginFormState loginFormState) {
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

        loginViewModel.getMyData().observe(this, new Observer<AccountUserInfo>() {
            @Override
            public void onChanged(AccountUserInfo accountUserInfo) {
                Gson gson = new Gson();
                accountUserInfoJson = gson.toJson(accountUserInfo);
            }
        });

        /**
         * Observer
         * Function: observe the LoginResult class data change signal
         */
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged( LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
                if(loginResult.getWrong() != null) {
                    showLoginFailed(loginResult.getWrong());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });
    }

    /**
     * Date: 23.03.31
     * Function: set all the data and button listeners
     * Version: Week 10
     */
    private void setListeners () {
        /**
         * Function: listen the edittext widget input data change signal
         */
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

        /**
         * Function: listen the login button click event
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        /**
         * Function: listen the back button click event
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(home_intent);
            }
        });

        /**
         * Function: listen the pwdVisible button click event
         */
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



    /**
     * function of building dialog
     * @param str
     */
    private void midToast(String str)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_toast_custom,
                (ViewGroup) findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    private void midToast(int strID){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_toast_custom,
                (ViewGroup) findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(strID);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
    /**
     * process the valid user login event - page jump & show dialog
     * @param model
     */

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome =  model.getDisplayName();
        // TODO : initiate successful logged in experience
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        midToast(welcome);

        Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main_intent);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("accountUserInfoJson", accountUserInfoJson);
        startActivity(intent);
    }
    /**
     * process the failed login
     * @param failure
     */
    private void showLoginFailed(String failure){
        midToast(failure);
    }

}