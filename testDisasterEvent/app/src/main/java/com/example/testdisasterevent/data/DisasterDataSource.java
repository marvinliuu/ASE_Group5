package com.example.testdisasterevent.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.DisasterDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisasterDataSource {
    public DisasterDetail[] details;

    /**
     * Date: 23.04.14
     * Function: get disaster details info from database
     * Version: Week 12
     */
    public LiveData<DisasterDetail[]> getDisasterDetails() {
        final MutableLiveData<DisasterDetail[]> disasterLiveData = new MutableLiveData<>();
        if (details != null) {
            disasterLiveData.setValue(details);
            return disasterLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("DisasterInfo");
        // real data
        long startOfDay = System.currentTimeMillis() - 43200000*3;
        // fake and test data

        long endOfDay = System.currentTimeMillis() + 86400000;


        Query postsQuery = postsRef.orderByChild("happenTime").startAt(startOfDay).endAt(endOfDay);
        // post query to database
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                details = new DisasterDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long happenTime = postSnapshot.child("happenTime").getValue(Long.class);
                    //String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(otime));
                    int radius = postSnapshot.child("radius").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    String rtype = postSnapshot.child("type").getValue(String.class);
                    int injury=postSnapshot.child("injury").getValue(int.class);
                    long gardaID = postSnapshot.child("GardaID").getValue(Long.class);
                    details[count++] = new DisasterDetail(radius, location, happenTime, latitude,
                            longitude, injury,rtype,gardaID,0);                }
                disasterLiveData.setValue(details);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

        return disasterLiveData;
    }
}
