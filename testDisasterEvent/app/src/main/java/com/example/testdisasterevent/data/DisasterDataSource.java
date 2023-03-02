package com.example.testdisasterevent.data;

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
    private String[] disasterTitles = {"Fire", "Water", "General"};
    public DisasterDetail[] details;

    public LiveData<DisasterDetail[]> getDisasterDetails() {
        final MutableLiveData<DisasterDetail[]> disasterLiveData = new MutableLiveData<>();
        if (details != null) {
            disasterLiveData.setValue(details);
            return disasterLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("DisasterInfo");
        // real data
        long startOfDay = System.currentTimeMillis() - 43200000;
        // fake and test data
        startOfDay = System.currentTimeMillis() - 43200000 * 5;
        long endOfDay = System.currentTimeMillis() + 86400000;

//        long startOfDay = new GregorianCalendar(2023, Calendar.MARCH, 17).getTimeInMillis();
//        long endOfDay = new GregorianCalendar(2023, Calendar.MARCH, 18).getTimeInMillis();

        Query postsQuery = postsRef.orderByChild("otime").startAt(startOfDay).endAt(endOfDay);

        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                details = new DisasterDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long otime = postSnapshot.child("otime").getValue(Long.class);
                    String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(otime));
                    int radius = postSnapshot.child("radius").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    int rtype = postSnapshot.child("disasterType").getValue(int.class);
                    details[count++] = new DisasterDetail(radius, location, happenTime, latitude,
                            longitude, disasterTitles[rtype - 1]);                }
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
