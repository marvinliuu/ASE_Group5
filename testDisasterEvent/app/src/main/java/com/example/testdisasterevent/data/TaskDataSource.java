package com.example.testdisasterevent.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.MainActivity;
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
    public TaskDetail[] details;

    /**
     * Date: 23.04.14
     * Function: get task details info from database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public LiveData<TaskDetail[]> getTaskDetails() {
        final MutableLiveData<TaskDetail[]> taskLiveData = new MutableLiveData<>();

        if (details != null) {
            taskLiveData.setValue(details);
            return taskLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("TaskInfo");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TaskDetail earliestDetail = null;
                long minOtime = Long.MAX_VALUE;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Long uid = postSnapshot.child("uid").getValue(Long.class);
                    if (uid != null && uid.equals(MainActivity.currentUserId)) {
                        Long otime = postSnapshot.child("otime").getValue(Long.class);
                        if (otime != null && otime < minOtime) {
                            minOtime = otime;
                            String happenTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(otime));
                            float latitude = postSnapshot.child("latitude").getValue(float.class);
                            float longitude = postSnapshot.child("longitude").getValue(float.class);
                            String location = postSnapshot.child("location").getValue(String.class);
                            String rtype = postSnapshot.child("disasterType").getValue(String.class);
                            int injury = postSnapshot.child("injury").getValue(int.class);

                            earliestDetail = new TaskDetail(latitude, longitude, injury, rtype, happenTime, location, "0");
                        }
                    }
                }
                details = new TaskDetail[1];
                details[0] = earliestDetail;
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
