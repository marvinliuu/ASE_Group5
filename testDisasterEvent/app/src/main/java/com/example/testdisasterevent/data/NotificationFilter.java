package com.example.testdisasterevent.data;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.example.testdisasterevent.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationFilter {
    /**
     * Date: 23.04.14
     * Function: add task listener of the task data in order to get he latest task for the user
     * Author: Siyu Liao
     * Version: Week 12
     */
    public void addTaskListener(Context context, long uid){
        DatabaseReference mReference;
        mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasks = mReference.child("TaskInfo");

        tasks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.hasChild("uid")) {
                    long taskID = snapshot.child("uid").getValue(Long.class);
                    String state = snapshot.child("state").getValue(String.class);
                    if(taskID == uid && !state.equals("1")) {
                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        String channelID = "AAA";
                        Notification notification = new Notification.Builder(context, channelID)
                                .setContentTitle("You have a new task.")
                                .setContentText(snapshot.child("task").getValue(String.class))
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .build();
                        NotificationChannel channel = new NotificationChannel(channelID, "channelName", NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                        manager.notify(1123, notification);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
