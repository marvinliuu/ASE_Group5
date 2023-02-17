package com.example.testdisasterevent.ui.register;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.data.RegisterDataSource;
import com.example.testdisasterevent.data.RegisterRepository;


public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create( Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(RegisterRepository.getInstance(new RegisterDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}