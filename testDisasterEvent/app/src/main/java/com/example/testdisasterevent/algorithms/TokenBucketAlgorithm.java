package com.example.testdisasterevent.algorithms;

import android.util.Log;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.Result;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.LoggedInUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TokenBucketAlgorithm {
        private int capacity;
        private int tokens;
        private long lastRefillTime;
        private long refillRate;

        public TokenBucketAlgorithm(int capacity) {
            this.capacity = capacity;
            getTokenInfoFromDatabase();
        }

        public synchronized boolean allowRequest() {
            long currentTime = System.currentTimeMillis();
            refill(currentTime);

            if (tokens > 0) {
                tokens--;
                writeBackToDatabase(tokens, 0);
                return true;
            } else {
                return false;
            }
        }

        public void refill() {
            long currentTime = System.currentTimeMillis();
            refill(currentTime);
        }

        private void refill(long currentTime) {
            long timeSinceLastRefill = currentTime - lastRefillTime;
            int tokensToAdd = (int) (timeSinceLastRefill * capacity / this.refillRate);
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = currentTime;
            writeBackToDatabase(tokens, lastRefillTime);
        }

        private void getTokenInfoFromDatabase() {
            // can be launched in a separate asynchronous job
            DatabaseReference mReference;
            mReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference tokeninfo = mReference.child("TokenBuket");
            tokeninfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    updateTokenInfo(dataSnapshot);
                }
                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

        private void writeBackToDatabase(int newTokens, long newLastRefillTime) {
            if (newLastRefillTime == 0) {
                newLastRefillTime = lastRefillTime;
            }

            // Write token data to Firebase Realtime Database
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference tokenDatabase = mDatabase.child("TokenBuket");

            long finalNewLastRefillTime = newLastRefillTime;
            tokenDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Write user data into the database
                    Map<String, Object> tokenData = new HashMap<>();
                    tokenData.put("Availble_Token", newTokens);
                    tokenData.put("lastRefillTime", finalNewLastRefillTime);
                    tokenData.put("refillRate", refillRate);
                    tokenData.put("TotalToken", capacity);

                    mDatabase.child("TokenBuket").setValue(tokenData);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(null, "onCancelled", databaseError.toException());
                }
            });

        }

        private void updateTokenInfo(DataSnapshot token) {
            this.tokens = token.child("Availble_Token").getValue(int.class);
            this.lastRefillTime = token.child("lastRefillTime").getValue(long.class);
            this.refillRate = token.child("refillRate").getValue(long.class);
            this.capacity = token.child("TotalToken").getValue(int.class);
            // Refill tokens at the specified refill rate
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(refillRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    refill();
                }
            }).start();
        }
}
