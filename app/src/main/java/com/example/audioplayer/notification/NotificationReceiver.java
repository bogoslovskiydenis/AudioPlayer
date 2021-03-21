package com.example.audioplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.audioplayer.service.MusicService;

import static com.example.audioplayer.aplication.ApplicationClass.ACTION_NEXT;
import static com.example.audioplayer.aplication.ApplicationClass.ACTION_PLAY;
import static com.example.audioplayer.aplication.ApplicationClass.ACTION_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if(actionName != null ){
            switch (actionName){
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName", "actionPlay");
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName", "next");
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    serviceIntent.putExtra("ActionName", "previous");
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}
