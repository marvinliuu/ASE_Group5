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

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;
    private TextView accountType;
    private TextView name;
    private TextView mail;
    private TextView mobile;


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);
        MainActivity mainActivity = (MainActivity) getActivity();
        AccountUserInfo accountUserInfoData = mainActivity.getAccountUserInfo();
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        name = binding.accountNameInfo;
        mail = binding.accountEmailInfo;
        mobile = binding.accountMobileInfo;
        accountType = binding.accountTypeText;
        mViewModel.getAccountUserInfo().observe(getActivity(), new Observer<AccountUserInfo>() {
            @Override
            public void onChanged(AccountUserInfo accountUserInfo) {
                if (accountUserInfoData != null) {
                    name.setText(accountUserInfoData.getUsername());
                    mail.setText(accountUserInfoData.getEmail());
                    mobile.setText(accountUserInfoData.getMobile());
                    accountType.setText(accountUserInfoData.getUserType());
//                    accountType.setText(1);
                }
            }
        });
        return root;
//        return inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        // TODO: Use the ViewModel
    }

}