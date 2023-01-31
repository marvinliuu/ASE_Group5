package com.example.testdisasterevent.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testdisasterevent.HomeActivity;
import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.example.testdisasterevent.databinding.RegisterFragmentBinding;
import com.example.testdisasterevent.ui.login.LoginActivity;
import com.example.testdisasterevent.ui.login.LoginViewModel;
import com.example.testdisasterevent.ui.login.LoginViewModelFactory;
import com.example.testdisasterevent.ui.register.RegisterFormState;
import com.example.testdisasterevent.ui.register.RegisterViewModel;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private RegisterFragmentBinding binding;
    private Button registerBack;
    private Button registerFinish;
    private Button pwdVisible;
    private Button actDescription;
    private EditText nickNameText;
    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPwd;
    private EditText registerActCode;
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = RegisterFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registerBack = binding.registerBack;
        registerFinish = binding.registerFinish;
        nickNameText = binding.registerName;
        registerName = binding.registerName;
        registerEmail = binding.registerEmail;
        registerPwd = binding.registerPassword;
        registerActCode = binding.registerActivationCode;

        pwdVisible = binding.passwordVisible;
        actDescription = binding.ActivationExplanation;


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);
        // TODO: Use the ViewModel

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
                mViewModel.RegisterDataChanged(registerName.getText().toString(), registerPwd.getText().toString(),
                        registerEmail.getText().toString(), registerActCode.getText().toString());
            }
        };
        registerEmail.addTextChangedListener(afterTextChangedListener);
        registerPwd.addTextChangedListener(afterTextChangedListener);
        registerName.addTextChangedListener(afterTextChangedListener);
        registerActCode.addTextChangedListener(afterTextChangedListener);

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(login_intent);
            }
        });
        registerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String pwd = registerPwd.getText().toString();
                String actCode = registerActCode.getText().toString();


                mViewModel.register(name, pwd, email, actCode);

                Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(login_intent);
            }
        });

        mViewModel.getRegisterFormState().observe(getActivity(), new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerFinish.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getEmailError() != null) {
                    registerEmail.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getUsernameError() != null) {
                    registerName.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    registerPwd.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getActCodeError() != null) {
                    registerActCode.setError(getString(registerFormState.getActCodeError()));
                }
            }
        });


    }

}