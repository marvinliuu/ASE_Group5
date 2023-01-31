package com.example.testdisasterevent.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.testdisasterevent.data.LoginDataSource;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.RegisterDataSource;
import com.example.testdisasterevent.data.RegisterRepository;
import com.example.testdisasterevent.ui.register.RegisterViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        }
//        } else if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
//            return (T) new RegisterViewModel(RegisterRepository.getInstance(new RegisterDataSource()));
//        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}