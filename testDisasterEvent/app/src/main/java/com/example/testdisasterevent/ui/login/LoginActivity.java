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
import com.example.testdisasterevent.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private HideReturnsTransformationMethod method_show;
    private ActivityLoginBinding binding;
    private ImageView pwdVisible;
    private boolean pwdFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // bind the certain activity xml
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create new object of the view model
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // get the certain widget in the activity xml
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button backButton = binding.loginback;
        final ProgressBar loadingProgressBar = binding.loading;
        // set image of the imageview
        final ImageView logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(this).load(R.drawable.disaster_fire_logo).into(logo);
        loginButton.setEnabled(false);
        pwdVisible = binding.passwordVisible;


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

        /**
         * Listener
         * Function: listen the edittext widget input data change signal
         */
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

        /**
         * Listener
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
         * Listener
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
         * Listener
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
     * process the valid user login event - page jump & show dialog
     * @param model
     */
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main_intent);
    }

    /**
     * process the invalid user login event - show dialog
     * @param errorString
     */
    private void showLoginFailed(Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}