package com.example.testdisasterevent.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.TaskDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDataSource {
    private String[] disasterTitles = {"Fire", "Water", "General"};
    public TaskDetail[] details;

    public LiveData<TaskDetail[]> getTaskDetails() {
        final MutableLiveData<TaskDetail[]> taskLiveData = new MutableLiveData<>();
        if (details != null) {
            taskLiveData.setValue(details);
            return taskLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("TaskInfo");
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
                details = new TaskDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long otime = postSnapshot.child("otime").getValue(Long.class);
                    String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(otime));
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    String rtype = postSnapshot.child("disasterType").getValue(String.class);
                    int injury = postSnapshot.child("injury").getValue(int.class);

                    details[count++] = new TaskDetail(location, latitude,
                            longitude, injury, rtype, happenTime);                }
                taskLiveData.setValue(details);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

        return taskLiveData;
    }
}
