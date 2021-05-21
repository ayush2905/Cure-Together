package com.example.curetogether.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.curetogether.App;
import com.example.curetogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class PushNotification extends FirebaseMessagingService {

//    @Override
//    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_MESSAGE_ID)
//                .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                .setContentTitle("Title")
//                .setContentText("Text")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .build();
//        manager.notify(1, notification);
//    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        saveToken(s);
    }

    public static void saveToken(String token) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return;
        FirebaseDatabase.getInstance()
                .getReference()
                .child("token")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(token);
    }
}
