package com.example.testdisasterevent.ui.account;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.databinding.FragmentAccountBinding;
import com.google.gson.Gson;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;  // Type: FragmentAccountBinding, used for binding data to the UI
    private TextView accountType;  // Type: TextView, used for displaying the account type
    private TextView name;  // Type: TextView, used for displaying the user's name
    private TextView mail;  // Type: TextView, used for displaying the user's email
    private TextView mobile;  // Type: TextView, used for displaying the user's mobile number

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity mainActivity = (MainActivity) getActivity();
        AccountUserInfo accountUserInfoData = mainActivity.getAccountUserInfo();

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.accountNameInfo;
        mail = binding.accountEmailInfo;
        mobile = binding.accountMobileInfo;
        accountType = binding.accountTypeText;
        if (accountUserInfoData != null) {
            name.setText(accountUserInfoData.getUsername());
            mail.setText(accountUserInfoData.getEmail());
            mobile.setText(accountUserInfoData.getMobile());
            accountType.setText(accountUserInfoData.getUserType());
        }
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}