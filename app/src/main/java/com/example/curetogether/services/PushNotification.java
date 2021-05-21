package com.example.curetogether.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.curetogether.App;
import com.example.curetogether.R;
import com.example.curetogether.activity.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class PushNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_MESSAGE_ID)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

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
