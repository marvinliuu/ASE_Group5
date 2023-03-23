package com.example.testdisasterevent.data;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.testdisasterevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MessagingService extends FirebaseMessagingService {
    public static String TAG = "tag_-----MessagingService:";

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From Id is: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

//        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
//            sendNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        } else {
//            sendNotification(getApplicationContext(), remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
//        }
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    private void sendNotification(Context iContext, String messageTitle, String messageBody) {
        NotificationManager notificationManager = (NotificationManager) iContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, MessageActivity.class);

//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {500, 500, 500, 500, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "-1")
                .setTicker(messageTitle)
                //.setSmallIcon(R.drawable.dr)
                .setContentTitle("push 通知 标题")
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setWhen(System.currentTimeMillis())
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE);

//        builder.setContentIntent(pendingIntent);
//        builder.setFullScreenIntent(pendingIntent, true);

        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }

    private void sendRegistrationToServer(String token){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("FCMToken");
        mDatabase.child(token.substring(0,6)).child("Token").setValue(token);
        mDatabase.child(token.substring(0,6)).child("rTime").setValue(System.currentTimeMillis());
        //        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                snapshot.child("").
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

    }
}

