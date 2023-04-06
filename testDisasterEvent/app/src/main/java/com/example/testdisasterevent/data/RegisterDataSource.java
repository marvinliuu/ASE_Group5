package com.example.testdisasterevent.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
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
    public final String TAG = "RegisterDataSource";
    private String userId;
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
            DatabaseReference countDatabase = mDatabase.child("count");
            userId = "UserInfo";

            userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Save the number of users
                    count = dataSnapshot.getChildrenCount() + 1;
                    countDatabase.setValue(count);

                    // Write user data into the database
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("mail", email);
                    userData.put("name", username);
                    userData.put("password", PasswordEncryption.encryptPassword(password));
                    userData.put("uid", count);
                    userData.put("r-time", generateRandomTime());

                    // Check if actCode is null or empty, and assign 0 as default
                    if (actCode == null || actCode.isEmpty()) {
                        userData.put("type", 0);
                    } else {
                        int actCodeInt = Integer.parseInt(actCode);
                        userData.put("type", actCodeInt); // Change to int
                    }

                    userData.put("phone", phone);

                    userId += count;
                    userDatabase.child(userId).setValue(userData);

                    String tempKey = mDatabase.child("AvailableOfficer").push().getKey();
                    Map<String, Object> ava_data = new HashMap<>();
                    ava_data.put("uid", count);
                    ava_data.put("type", 1);
                    mDatabase.child("AvailableOfficer").child(tempKey).setValue(ava_data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "onCancelled", databaseError.toException());
                }
            });

            return new Result.Success<>("Register Success" + username);
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
        // TODO: check if type valid
        this.userType = userType;
    }
}

