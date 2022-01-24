package com.example.audioplayer.service;

import static com.example.audioplayer.PlayerActivity.listSongs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.audioplayer.action.ActionPlay;
import com.example.audioplayer.action.Actions;
import com.example.audioplayer.model.MusicFiles;
import com.example.audioplayer.notification.NotificationUi;
import com.example.audioplayer.utils.BytesFromUri;
import com.example.audioplayer.utils.FormatTimeUi;

import java.util.ArrayList;


public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    int position = 0;
    ActionPlay actionPlaying;
    MediaSessionCompat mediaSessionCompat;
    private final BytesFromUri bytesFromUri = new BytesFromUri.Base();
    public NotificationUi notificationUi;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat =new MediaSessionCompat(getBaseContext(), "My Audio");
        notificationUi = new NotificationUi.Base(this, mediaSessionCompat, bytesFromUri);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Bind", "Method");
        return mBinder;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        Actions.Previous previous = new Actions.Previous();
        Actions.Play play = new Actions.Play();
        Actions.Next next = new Actions.Next();
        if (myPosition != -1) {
            playMedia(myPosition);
        }
        if (actionName != null) {
            if (previous.compare(actionName)) {
                previous.showToast(this);
                play.act(actionPlaying);
            } else {
                if (play.compare(actionName)) {
                    play.showToast(this);
                    play.act(actionPlaying);
                } else {
                    if (next.compare(actionName)) {
                        next.showToast(this);
                        next.act(actionPlaying);
                    }
                }
            }
        }
        return START_STICKY;
    }

    private void playMedia(int StartPosition) {
        musicFiles = listSongs;
        position = StartPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void start() { mediaPlayer.start(); }
    public boolean isPlaying() { return mediaPlayer.isPlaying(); }
    public void stop() { mediaPlayer.stop(); }
    public void release() { mediaPlayer.release(); }
    public int getDuration() { return mediaPlayer.getDuration(); }
    public void seek(int position) { mediaPlayer.seekTo(position); }

    public void createMediaPlayer(int positionInner) {
        position = positionInner;
        Uri uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer != null) {
            actionPlaying.nextBtnClicked();
            if (mediaPlayer != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
                onCompleted();
            }
        }
    }

    public void setCallBack(ActionPlay actionPlaying) {
        this.actionPlaying = actionPlaying;
    }
}
