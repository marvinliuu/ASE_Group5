package com.example.testdisasterevent.ui.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.data.LoginDataSource;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.RegisterDataSource;
import com.example.testdisasterevent.data.RegisterRepository;
import com.example.testdisasterevent.ui.login.LoginViewModel;


public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(RegisterRepository.getInstance(new RegisterDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}