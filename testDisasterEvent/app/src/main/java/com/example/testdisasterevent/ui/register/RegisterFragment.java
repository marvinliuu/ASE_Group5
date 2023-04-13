package com.example.testdisasterevent.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.testdisasterevent.HomeActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.databinding.RegisterFragmentBinding;
import com.example.testdisasterevent.ui.login.LoginActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterFragment extends Fragment {
    private HideReturnsTransformationMethod method_show;
    private RegisterViewModel mViewModel;
    private RegisterFragmentBinding binding;
    private Button registerBack, registerFinish;
    private ImageView pwdVisible;
    private Button actDescription;
    private EditText registerName, registerEmail;
    private EditText registerPwd, registerPhone;
    private EditText registerActCode;
    private boolean pwdFlag = false;
    private PopupWindow popupWindow;
    private View contentView;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {

        binding = RegisterFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registerBack = binding.registerBack;
        registerFinish = binding.registerFinish;
        registerName = binding.registerName;
        registerEmail = binding.registerEmail;
        registerPwd = binding.registerPassword;
        registerPhone = binding.registerPhone;
        registerActCode = binding.registerActivationCode;

        pwdVisible = binding.passwordVisible;
        actDescription = binding.ActivationExplanation;
        registerFinish.setEnabled(false);

        return root;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showPopwindow();
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
        registerPhone.addTextChangedListener(afterTextChangedListener);
        registerActCode.addTextChangedListener(afterTextChangedListener);

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(login_intent);
            }
        });

        mViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<RegisterResult>() {
            @Override
            public void onChanged(RegisterResult registerResult) {
                if(registerResult.getStatus().equals("success")){
                    String success = "Registration successful!";
                    midToast(success);
                    Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login_intent);
                } else if(registerResult.getStatus().equals("fail")){
                    String failure = "Registration failed!";
                    midToast(failure);
                    Intent res_intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(res_intent);
                } else if(registerResult.getStatus().equals("repeated")){
                    String repeated = "This E-mail address has been registered!";
                    midToast(repeated);
                    Intent res_intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(res_intent);
                }
            }
        });
        pwdVisible.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PasswordTransformationMethod methodHide=PasswordTransformationMethod.getInstance();
                registerPwd.setTransformationMethod(methodHide);
                if(pwdFlag){
                    pwdVisible.setImageResource(R.drawable.eye1);
                    pwdFlag=false;
                    PasswordTransformationMethod method_hide = PasswordTransformationMethod.getInstance();
                    registerPwd.setTransformationMethod(method_hide);
                }
                else{
                    pwdVisible.setImageResource(R.drawable.eye2);
                    pwdFlag=true;
                    method_show= HideReturnsTransformationMethod.getInstance();
                    registerPwd.setTransformationMethod(method_show);
                }
            }
        });

        registerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String pwd = registerPwd.getText().toString();
                String phone = registerPhone.getText().toString();
                String actCode = registerActCode.getText().toString();
                mViewModel.register(name, pwd, email, phone, actCode);

            }
        });
        actDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }
        });

        mViewModel.getRegisterFormState().observe(getActivity(), new Observer<RegisterFormState>() {
            @Override
            public void onChanged( RegisterFormState registerFormState) {
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

    private void midToast(String str) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_toast_custom,
                (ViewGroup) getView().findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    /**
     * show popupWindow
     */
    private void showPopwindow() {
        //load popup window layout
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.register_popupwindow, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                700);
        // set SelectPicPopupWindow height
        popupWindow.setHeight(700);
        // get focus point
        popupWindow.setFocusable(true);
        // set background color of blank area
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // Click outside to disappear
        popupWindow.setOutsideTouchable(true);
        // Settings can be clicked
        popupWindow.setTouchable(true);
        // hidden animation
        popupWindow.setAnimationStyle(R.style.ipopwindow_anim_style);
    }


}