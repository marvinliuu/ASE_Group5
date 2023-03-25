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
    private String[] reportTitles = {"Fire", "Water", "General"};
    public ReportInfo[] infos;

    public LiveData<ReportInfo[]> getReportInfo() {
        final MutableLiveData<ReportInfo[]> reportLiveData = new MutableLiveData<>();
        if (infos != null) {
            reportLiveData.setValue(infos);
            return reportLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Report");
        // real data
        long startOfDay = System.currentTimeMillis() - 43200000;
        // fake and test data
//        startOfDay = System.currentTimeMillis() - 43200000 * 10;
//        long endOfDay = startOfDay + 86400000;
        startOfDay = System.currentTimeMillis() - 43200000 * 5;
        long endOfDay = System.currentTimeMillis() + 86400000;

//        long startOfDay = new GregorianCalendar(2023, Calendar.MARCH, 17).getTimeInMillis();
//        long endOfDay = new GregorianCalendar(2023, Calendar.MARCH, 18).getTimeInMillis();

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
//                    int injured = postSnapshot.child("injury").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    int rtype = postSnapshot.child("report_type").getValue(int.class);
//                    String description = postSnapshot.child("description").getValue(String.class);
                    int reportState = postSnapshot.child("report_state").getValue(int.class);
                    infos[count++] = new ReportInfo(location, happenTime, latitude,
                            longitude, reportTitles[rtype - 1], reportState, postSnapshot.getKey());
//                    System.out.println(21212121);
//                    System.out.println(postSnapshot.getKey());


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
