package com.example.testdisasterevent.ui.register;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.RegisterRepository;
import com.example.testdisasterevent.data.Result;

import org.jetbrains.annotations.Nullable;



public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private RegisterRepository registerRepository;
    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }
    // TODO: Implement the ViewModel
    public boolean register(String username, String password, String email, String actCode) {
        // can be launched in a separate asynchronous job
        Result<String> result = registerRepository.register(username, password, email, actCode);

        if (result instanceof Result.Success) {
            return true;
        } else {
            return false;
        }
    }

    public void RegisterDataChanged(String username, String password, String email, String actCode) {
        if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_email, null));
        } else if (!isNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null, null));
        } else if ( !isActCodeValid(actCode)){
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_actCode));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check

    //Check if the input email is valid.
    public static boolean isEmailValid(String email) {
        String str = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
//    public boolean isEmailValid(String email) {
//        if (email == null) {
//            return false;
//        }
//        if (email.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        } else {
//            return !email.trim().isEmpty();
//        }
//    }

    // A placeholder password validation check
    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public boolean isActCodeValid(String actCode) {
        if (actCode == null) return true;
        else {
            return registerRepository.isActCodeValid(actCode);
        }
    }

    public boolean isNameValid(String name) {
        return name != null && name.trim().length() < 15;
    }
}




