package com.example.testdisasterevent.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;


public class RegisterFragment extends Fragment {
    private ImageView imageView;
    private HideReturnsTransformationMethod method_show;
    private RegisterViewModel mViewModel;
    private RegisterFragmentBinding binding;
    private Button registerBack;
    private Button registerFinish;
    private ImageView pwdVisible;
    private Button actDescription;
    private EditText nickNameText;
    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPwd;
    private EditText registerActCode;
    private boolean pwdFlag = false;
    private PopupWindow popupWindow;
    private View contentView;

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
        registerFinish.setEnabled(false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
        registerActCode.addTextChangedListener(afterTextChangedListener);

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(login_intent);
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
                String actCode = registerActCode.getText().toString();

                mViewModel.register(name, pwd, email, actCode);

                Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(login_intent);
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

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        //加载弹出框的布局
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.register_popupwindow, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                700);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(700);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.ipopwindow_anim_style);
    }


}