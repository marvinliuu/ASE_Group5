package com.example.testdisasterevent.ui.disaster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.DisasterDataSource;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DisaterViewModel extends ViewModel {

    private MutableLiveData<DisasterDetail[]> disasterDetails = new MutableLiveData<>();


    public LiveData<DisasterDetail[]> getDisasterDetails() {
        if (disasterDetails == null) {
            disasterDetails = new MutableLiveData<>();
        }
        return disasterDetails;
    }

    public DisaterViewModel() {
        readDisasterDetails();
    }

    private DisasterDetail[] details;
    private String[] disasterTitles = {"Fire", "Water", "General"};

    public void readDisasterDetails () {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Report");
        // real data
        long startOfDay = System.currentTimeMillis() - 43200000;
        // fack and test data
        startOfDay = System.currentTimeMillis() - 43200000 * 10;
        long endOfDay = startOfDay + 86400000;

//        long startOfDay = new GregorianCalendar(2023, Calendar.MARCH, 17).getTimeInMillis();
//        long endOfDay = new GregorianCalendar(2023, Calendar.MARCH, 18).getTimeInMillis();

        Query postsQuery = postsRef.orderByChild("htime").startAt(startOfDay).endAt(endOfDay);

        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                details = new DisasterDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long htime = postSnapshot.child("htime").getValue(Long.class);
                    String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(htime));

                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longtitude = postSnapshot.child("longitude").getValue(float.class);

                    String location = postSnapshot.child("location").getValue(String.class);
                    int rtype = postSnapshot.child("report_type").getValue(int.class);
                    details[count++] = new DisasterDetail(location, happenTime, latitude, longtitude, disasterTitles[rtype - 1]);
                }
                disasterDetails.setValue(details);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

}