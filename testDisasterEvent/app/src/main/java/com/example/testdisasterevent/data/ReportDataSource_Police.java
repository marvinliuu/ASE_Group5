package com.example.testdisasterevent.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.ReportInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportDataSource_Police {
    public ReportInfo[] infos;

    /**
     * Date: 23.04.14
     * Function: get unconfirm report info for police from database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public LiveData<ReportInfo[]> getReportInfo() {
        final MutableLiveData<ReportInfo[]> reportLiveData = new MutableLiveData<>();
        if (infos != null) {
            reportLiveData.setValue(infos);
            return reportLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Report");
        // real data
        long startOfDay = System.currentTimeMillis() - 43200000;

        long endOfDay = System.currentTimeMillis() + 86400000;

        Query postsQuery = postsRef.orderByChild("htime").startAt(startOfDay).endAt(endOfDay);

        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                infos = new ReportInfo[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long htime = postSnapshot.child("htime").getValue(Long.class);
                    String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(htime));
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    String rtype = postSnapshot.child("report_type").getValue(String.class);
                    int reportState = postSnapshot.child("report_state").getValue(int.class);
                    infos[count++] = new ReportInfo(location, happenTime, latitude,
                            longitude, rtype, reportState, postSnapshot.getKey());
                }
                reportLiveData.setValue(infos);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

        return reportLiveData;
    }
}
