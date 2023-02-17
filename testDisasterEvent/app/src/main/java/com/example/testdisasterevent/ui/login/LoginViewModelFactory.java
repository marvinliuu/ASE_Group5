package com.example.testdisasterevent.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.data.LoginDataSource;
import com.example.testdisasterevent.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create( Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}