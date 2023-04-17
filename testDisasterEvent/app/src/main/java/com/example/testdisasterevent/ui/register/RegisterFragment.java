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

    // onCreateView method called when the fragment's view is created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment's layout using data binding
        binding = RegisterFragmentBinding.inflate(inflater, container, false);
        // Get the root view of the inflated layout
        View root = binding.getRoot();

        // Assign views from the layout to local variables
        registerBack = binding.registerBack;
        registerFinish = binding.registerFinish;
        registerName = binding.registerName;
        registerEmail = binding.registerEmail;
        registerPwd = binding.registerPassword;
        registerPhone = binding.registerPhone;
        registerActCode = binding.registerActivationCode;

        // Assign additional views from the layout to local variables
        pwdVisible = binding.passwordVisible;
        actDescription = binding.ActivationExplanation;
        // Disable the 'registerFinish' button by default
        registerFinish.setEnabled(false);

        // Return the root view of the inflated layout
        return root;
    }

    // onActivityCreated method called when the fragment's activity has been created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showPopwindow();
        // Initialize the ViewModel with a factory
        mViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        // TextWatcher to listen for text changes in input fields
        TextWatcher afterTextChangedListener = new TextWatcher() {
            // Ignore beforeTextChanged event
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Ignore onTextChanged event
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            // Handle the afterTextChanged event
            @Override
            public void afterTextChanged(Editable s) {
                // Update the ViewModel with the new input data
                mViewModel.RegisterDataChanged(registerName.getText().toString(), registerPwd.getText().toString(),
                        registerEmail.getText().toString(), registerActCode.getText().toString());
            }
        };
        // Add the TextWatcher to input fields
        registerEmail.addTextChangedListener(afterTextChangedListener);
        registerPwd.addTextChangedListener(afterTextChangedListener);
        registerName.addTextChangedListener(afterTextChangedListener);
        registerPhone.addTextChangedListener(afterTextChangedListener);
        registerActCode.addTextChangedListener(afterTextChangedListener);

        // Set an OnClickListener for the 'registerBack' button
        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the HomeActivity when the button is clicked
                Intent login_intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(login_intent);
            }
        });

        // Observe the registration result LiveData from the ViewModel
        mViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<RegisterResult>() {
            @Override
            public void onChanged(RegisterResult registerResult) {
                // Handle different registration result statuses
                if (registerResult.getStatus().equals("success")) {
                    String success = "Registration successful!";
                    midToast(success);
                    // Start the LoginActivity on successful registration
                    Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login_intent);
                } else if (registerResult.getStatus().equals("fail")) {
                    String failure = "Registration failed!";
                    midToast(failure);
                    // Restart the RegisterActivity on failed registration
                    Intent res_intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(res_intent);
                } else if (registerResult.getStatus().equals("repeated")) {
                    String repeated = "This E-mail address has been registered!";
                    midToast(repeated);
                    Intent res_intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(res_intent);
                }
            }
        });
        // Set an OnClickListener for the 'pwdVisible' button (show/hide password)
        pwdVisible.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PasswordTransformationMethod methodHide = PasswordTransformationMethod.getInstance();
                registerPwd.setTransformationMethod(methodHide);
                // Toggle password visibility based on the 'pwdFlag' state
                if (pwdFlag) {
                    pwdVisible.setImageResource(R.drawable.eye1);
                    pwdFlag = false;
                    PasswordTransformationMethod method_hide = PasswordTransformationMethod.getInstance();
                    registerPwd.setTransformationMethod(method_hide);
                } else {
                    pwdVisible.setImageResource(R.drawable.eye2);
                    pwdFlag = true;
                    method_show = HideReturnsTransformationMethod.getInstance();
                    registerPwd.setTransformationMethod(method_show);
                }
            }
        });

// Set an OnClickListener for the 'registerFinish' button (submit registration)
        registerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input data from the input fields
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String pwd = registerPwd.getText().toString();
                String phone = registerPhone.getText().toString();
                String actCode = registerActCode.getText().toString();
                // Call the ViewModel's register method with the input data
                mViewModel.register(name, pwd, email, phone, actCode);
            }
        });

// Set an OnClickListener for the 'actDescription' button (show activation code explanation)
        actDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the popup window at the bottom of the contentView
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }
        });

// Observe the registration form state LiveData from the ViewModel
        mViewModel.getRegisterFormState().observe(getActivity(), new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                // If the form state is null, return
                if (registerFormState == null) {
                    return;
                }
                // Enable or disable the 'registerFinish' button based on the form state validity
                registerFinish.setEnabled(registerFormState.isDataValid());
                // Show error messages for input fields if needed
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

    // Custom method to display a toast message with a custom view
    private void midToast(String str) {
        // Get the LayoutInflater from the current activity
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the custom toast layout
        View view = inflater.inflate(R.layout.view_toast_custom,
                (ViewGroup) getView().findViewById(R.id.lly_toast));
        // Find the TextView within the custom layout and set its text
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);

        // Create a new Toast object with the activity's context
        Toast toast = new Toast(getActivity().getApplicationContext());
        // Set the toast's position to be centered horizontally and slightly above the vertical center
        toast.setGravity(Gravity.CENTER, 0, 10);
        // Set the toast's duration to be long
        toast.setDuration(Toast.LENGTH_LONG);
        // Set the toast's view to the custom view
        toast.setView(view);
        // Display the toast
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