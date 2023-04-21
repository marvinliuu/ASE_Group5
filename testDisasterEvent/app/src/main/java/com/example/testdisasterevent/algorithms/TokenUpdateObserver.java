package com.example.testdisasterevent.algorithms;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TokenUpdateObserver {
    private long refillRate;
    private int capacity;
    private TokenBucketAlgorithm tokenAlgorithm;

    public TokenUpdateObserver() {
        this.refillRate = 3600000;
        getTokenInfoFromDatabase();
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

    private void updateTokenInfo(DataSnapshot tokens) {
        this.refillRate = tokens.child("refillRate").getValue(long.class);
        this.capacity = tokens.child("TotalToken").getValue(int.class);
        tokenAlgorithm = new TokenBucketAlgorithm(this.capacity);

        // Refill tokens at the specified refill rate
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(refillRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tokenAlgorithm.refill();
            }
        }).start();
    }
}
