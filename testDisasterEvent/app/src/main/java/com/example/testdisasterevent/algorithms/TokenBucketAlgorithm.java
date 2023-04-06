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
        private int capacity;           // The maximum number of tokens that the bucket can hold
        private int tokens;             // The number of tokens currently in the bucket
        private long lastRefillTime;    // The time at which the bucket was last refilled
        private long refillRate;        // The rate at which the bucket refills (in milliseconds)


    /**
         * Constructor for the TokenBucketAlgorithm class.
         * Initializes the token bucket with the given capacity and retrieves the token information from the Firebase Realtime Database.
         *
         * @param capacity the capacity of the token bucket
         */
        public TokenBucketAlgorithm(int capacity) {
            this.capacity = capacity;
            getTokenInfoFromDatabase();
        }

        /**
         * Checks if a request can be allowed based on the current state of the token bucket.
         * If tokens are available, decrements the token count and writes the updated token information to the Firebase Realtime Database.
         *
         * @return true if the request can be allowed, false otherwise
         */
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

        /**
         * Refills the token bucket with tokens based on the refill rate and time elapsed since the last refill.
         * Should be called periodically to maintain a steady supply of tokens.
         */
        public void refill() {
            long currentTime = System.currentTimeMillis();
            refill(currentTime);
        }

        /**
         * Refills the token bucket with tokens based on the refill rate and time elapsed since the last refill.
         * Should be called periodically to maintain a steady supply of tokens.
         *
         * @param currentTime the current time to use for the refill calculation
         */
        private void refill(long currentTime) {
            long timeSinceLastRefill = currentTime - lastRefillTime;
            int tokensToAdd = (int) (timeSinceLastRefill * capacity / this.refillRate);
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = currentTime;
            writeBackToDatabase(tokens, lastRefillTime);
        }

        /**
         * Retrieves the token information from the Firebase Realtime Database and updates the token bucket state accordingly.
         * Should be called once at initialization to retrieve the initial state.
         */
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

        /**
         * Writes the updated token information to the Firebase Realtime Database.
         *
         * @param newTokens the updated token count
         * @param newLastRefillTime the updated last refill time
         */
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
