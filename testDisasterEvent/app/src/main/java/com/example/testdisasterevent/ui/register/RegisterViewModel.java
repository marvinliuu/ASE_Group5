package com.example.testdisasterevent.ui.register;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.RegisterRepository;
import com.example.testdisasterevent.data.Result;

import com.example.testdisasterevent.ui.register.RegisterResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private RegisterRepository registerRepository;
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }
    LiveData<RegisterResult> getRegisterResult() { return registerResult; }
    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    // TODO: Implement the ViewModel
    public void register(String username, String password, String email, String phone, String actCode) {
        // can be launched in a separate asynchronous job
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String status = "";
                for(DataSnapshot user : snapshot.getChildren()){
                    String testMail = user.child("mail").getValue(String.class);
                    if(testMail.equals(email)){
                        status = "repeated";
                        break;
                    }
                }
                if(!status.equals("repeated")){
                    Result<String> result = registerRepository.register(username, password, email, phone, actCode);
                    String resultStatus = ((Result.Success<String>) result).getData();
                    registerResult.setValue(new RegisterResult(resultStatus));
                }
                else{
                    registerResult.setValue(new RegisterResult(("repeated")));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

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


    //Check if the input email is valid.
    public static boolean isEmailValid(String email) {
        String str = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

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




