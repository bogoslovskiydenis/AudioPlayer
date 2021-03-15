package com.example.audioplayer.aplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {

    private static final String CHANNEL_ID_1 = "channel1";
    private static final String CHANNEL_ID_2 = "channel2";
    private static final String ACTION_PREVIOUS = "actionprevious";
    private static final String ACTION_NEXT = "actionnext";
    private static final String ACTION_PLAY = "actionplay";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChanel();
    }

    private void createNotificationChanel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1= new NotificationChannel(CHANNEL_ID_1,"channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel 1");

            NotificationManager notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }
}
