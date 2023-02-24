package com.example.testdisasterevent.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class RegisterDataSource {
    private static DatabaseReference mDatabase, UserDatabase, CountDatabase;
    private int userType;
    public static final String TAG = "YOUR-TAG-NAME";
    private String userId;
    private static long count;


    /**
     * process of register, with writing sequentially into firebase
     * @param username - user's email address
     * @param password - user's password
     * @param email - user's email
     * @param actCode - Activation Code for type
     */
    public Result<String>  register(String username, String password, String email, String actCode) {
        try {
            // Write user data to Firebase Realtime Database
            userId  = "UserInfo";
            mDatabase = FirebaseDatabase.getInstance().getReference();
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("UserInfo");
            CountDatabase = mDatabase.child("count");
            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // save the number of count
                    count = dataSnapshot.getChildrenCount() + 1;
                    CountDatabase.setValue(count);

                    CountDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the value from the DataSnapshot object
                            Long value = dataSnapshot.getValue(Long.class);

                            //write data into real time database
                            Map<String, String> userData = new HashMap<>();
                            userData.put("mail", email);
                            userData.put("name", username);
                            userData.put("password", password);
                            userData.put("uid", value.toString());
                            userData.put("r-time", generateRandomTime());
                            userData.put("type",actCode);
                            userData.put("phone","877022342");

                            userId += value;
                            mDatabase.child("UserInfo").child(userId).setValue(userData);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "onCancelled", databaseError.toException());
                }
            });


            return new Result.Success<>("Register Success" + username);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register", e));
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
