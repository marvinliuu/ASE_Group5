package com.example.testdisasterevent.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountViewModel extends ViewModel {
    private MutableLiveData<AccountUserInfo> accountUserInfo = new MutableLiveData<>();


    public LiveData<AccountUserInfo> getAccountUserInfo() {
        if (accountUserInfo == null) {
            accountUserInfo = new MutableLiveData<>();
        }
        return accountUserInfo;
    }
    public AccountViewModel() {
        readUserInfo();
    }
    public AccountUserInfo userInfo;
    private void readUserInfo() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        Query userQuery = userRef.orderByChild("uid").equalTo(1);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postShot : snapshot.getChildren()) {
                    String email = postShot.child("mail").getValue(String.class);
                    String name = postShot.child("name").getValue(String.class);
                    String mobile = postShot.child("phone").getValue(String.class);
                    long userTypeID = postShot.child("uid").getValue(long.class);
                    String userType = "";
                    if (userTypeID == 1) userType = "Citizen";
                    else if (userTypeID == 2) userType = "Doctor";
                    else if (userTypeID == 3) userType = "Fireman";
                    else userType = "Police";
                    userInfo = new AccountUserInfo(name, email, mobile, userType);
                    break;
                }
                accountUserInfo.setValue(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    // TODO: Implement the ViewModel
}