package com.example.audioplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.audioplayer.action.Actions;
import com.example.audioplayer.service.MusicService;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if(actionName !=null) {
            Actions.Previous previous = new Actions.Previous();
            if (previous.compare(actionName))
                previous.startService(context, serviceIntent);
            Actions.Play play = new Actions.Play();
            if (play.compare(actionName))
                play.startService(context, serviceIntent);
            Actions.Next next = new Actions.Next();
            if (play.compare(actionName))
                next.startService(context, serviceIntent);
        }
    }
}
