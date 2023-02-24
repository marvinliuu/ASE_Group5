package com.example.testdisasterevent.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.Result;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.LoggedInUser;
import com.example.testdisasterevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AccountUserInfo> accountUserInfoMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * process of login, with checking username and password
     * @param username - user's email address
     * @param password - user's password
     */
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        DatabaseReference mReference;
        mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = mReference.child("UserInfo");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccountUserInfo accountUserInfo;
                int loginStatus = 0;
                String displayName = "";
                long loginUserID = -1;
                  String email = "";
                String mobile = "";
                String registerTime = "";
                String userType = "";
                int userTypeId;
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.child("mail").getValue(String.class).equals(username)){
                        loginStatus = 1;
                        if(user.child("password").getValue(String.class).equals(password)){
                            displayName = user.child("name").getValue(String.class);
                            loginUserID = user.child("uid").getValue((long.class));
                            email = user.child("mail").getValue(String.class);
                            mobile = user.child("phone").getValue(String.class);
                            registerTime = user.child("r-time").getValue(String.class);
                            userTypeId = user.child("type").getValue(int.class);
                            if (userTypeId == 1) userType = "Citizen";
                            else if (userTypeId == 2) userType = "Doctor";
                            else if (userTypeId == 3) userType = "Fireman";
                            else userType = "Police";
                            accountUserInfo = new AccountUserInfo(email, displayName, password, mobile, registerTime, userTypeId, loginUserID, userType);
                            accountUserInfoMutableLiveData.setValue(accountUserInfo);
                            loginStatus = 2;
                        }
                        break;
                    }
                }
                Result<LoggedInUser> result = loginRepository.login(loginStatus, displayName, loginUserID);
                if (result instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                }
                else if(result instanceof Result.Failure) {
                    Integer data = ((Result.Failure) result).getStatus();
                    loginResult.setValue(new LoginResult(data));
                }
                else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    /**
     * check the validation when login data change
     * @param username - user's email address
     * @param password - user's password
     */
    public void loginDataChanged(String username, String password) {
        if (!isEmailValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**
     * A placeholder email validation check
     * @param email - user's email address
     * @return
     */
    public static boolean isEmailValid(String email) {
        String str = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public LiveData<AccountUserInfo> getMyData() {
        return accountUserInfoMutableLiveData;
    }

    /**
     * A placeholder password validation check
     * @param password - user's password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}