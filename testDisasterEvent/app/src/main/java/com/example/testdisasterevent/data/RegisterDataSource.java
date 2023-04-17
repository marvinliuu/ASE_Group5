package com.example.testdisasterevent.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.example.testdisasterevent.algorithms.PasswordEncryption;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class RegisterDataSource {
    private DatabaseReference mDatabase;
    private int userType;
    private long count;

    /**
     * process of register, with writing sequentially into firebase
     * @param username - user's email address
     * @param password - user's password
     * @param email - user's email
     * @param actCode - Activation Code for type
     */
    public Result<String> register(String username, String password, String email, String phone, String actCode) {
        try {
            // Write user data to Firebase Realtime Database
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userDatabase = mDatabase.child("UserInfo");
            String UserKey = userDatabase.push().getKey();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot root) {
                    DataSnapshot ICodes = root.child("IdentificationCode");
                    count = root.child("UserInfo").getChildrenCount();
                    String code;
                    Integer type = 0;
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("mail", email.toLowerCase(Locale.ROOT));
                    userData.put("name", username);
                    userData.put("password", PasswordEncryption.encryptPassword(password));
                    userData.put("uid", count + 1);
                    userData.put("r-time", generateRandomTime());
                    userData.put("phone", phone);
                    for(DataSnapshot ICode:ICodes.getChildren()){
                        code = ICode.child("number").getValue(String.class);
                        if(code.equals(actCode)){
                            type = ICode.child("type").getValue(Integer.class);
                            break;
                        }
                    }
                    userData.put("type", type);
                    userDatabase.child(UserKey).setValue(userData);
                    if(type != 0){
                        String tempKey = mDatabase.child("AvailableOfficer").push().getKey();
                        Map<String, Object> ava_data = new HashMap<>();
                        ava_data.put("uid", count + 1);
                        ava_data.put("type", type);
                        mDatabase.child("AvailableOfficer").child(tempKey).setValue(ava_data);
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


            return new Result.Success<>("success");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering user", e));
        }
    }

    // get the timetag for r-time
    @TargetApi(Build.VERSION_CODES.O)
    public static String generateRandomTime() {
        long randomTimeInSeconds = ThreadLocalRandom.current().nextLong(86400) * 60;
        LocalDateTime randomLocalDateTime = LocalDateTime.now().minusSeconds(randomTimeInSeconds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH-mm-ss");
        return randomLocalDateTime.format(formatter);
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

