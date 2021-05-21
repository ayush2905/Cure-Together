package com.example.curetogether;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_MESSAGE_ID = "Message";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_MESSAGE_ID,
                    CHANNEL_MESSAGE_ID,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setShowBadge(true);
            channel.setDescription("This is for message notification");
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
    }

}
