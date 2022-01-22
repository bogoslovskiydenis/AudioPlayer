package com.example.audioplayer;

import static com.example.audioplayer.MainActivity.musicFiles;
import static com.example.audioplayer.MainActivity.repeatBoolean;
import static com.example.audioplayer.MainActivity.shuffleBoolean;
import static com.example.audioplayer.adapter.AlbumDetailsAdapter.albumFiles;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.audioplayer.action.ActionPlay;
import com.example.audioplayer.listener.SimpleSeekBarChangeListener;
import com.example.audioplayer.model.MusicFiles;
import com.example.audioplayer.notification.NotificationUi;
import com.example.audioplayer.service.MusicService;
import com.example.audioplayer.utils.BytesFromUri;
import com.example.audioplayer.utils.FormatTimeUi;

import java.util.ArrayList;
import java.util.Random;

import soup.neumorphism.NeumorphFloatingActionButton;

public class PlayerActivity extends AppCompatActivity implements ActionPlay, ServiceConnection {

    TextView song_name, artist_name, duration_played, duration_total;
    SeekBar seekBar;
    ImageView cover_art, nextBtn, backBtn, prevBtn, shuffleBtn, repeatBtn;
    NeumorphFloatingActionButton playPauseBtn;
    int position = -1;
    public static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    public static Uri uri;
    private final Handler handler = new Handler();
    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;
    private final FormatTimeUi formatTime = new FormatTimeUi.Base();
    private final BytesFromUri bytesFromUri = new BytesFromUri.Base();
    private NotificationUi notificationUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFulScreen();
        setContentView(R.layout.player2);
       // getSupportActionBar().hide();
        notificationUi = new NotificationUi.Base(this, mediaSessionCompat, bytesFromUri);
        initViews();
        getIntentMethod();
        seekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seek(progress * 1000);
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int currentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    duration_played.setText(formatTime.format(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        //click shuffle btn
        shuffleBtn.setOnClickListener(v -> {
            shuffleBtn.setImageResource(shuffleBoolean
                    ? R.drawable.ic_shuffle_on
                    : R.drawable.ic_shuffle_off);
            shuffleBoolean = !shuffleBoolean;
        });
        //click repeat btn
        repeatBtn.setOnClickListener(v -> {
            repeatBtn.setImageResource(repeatBoolean ? R.drawable.ic_repeat_off : R.drawable.ic_repeat);
            repeatBoolean = !repeatBoolean;
        });

        playPauseBtn.setOnClickListener(v -> playPauseBtnClicked());
        nextBtn.setOnClickListener(v -> nextBtnClicked());
        prevBtn.setOnClickListener(v -> prevBtnClicked());

    }

    private void setFulScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    public void prevBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            //shuffle & repeat
            position = (position - 1) < 0 ? listSongs.size() - 1 : (position - 1);
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                }
            }, 1000);
            musicService.onCompleted();
            musicService.notificationUi.show(position);
           // notificationUi.show(position);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
            musicService.onCompleted();
        } else {
            musicService.stop();
            musicService.release();
            position = (position - 1) < 0 ? listSongs.size() - 1 : (position - 1);
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                }
            }, 1000);
            musicService.notificationUi.show(position);

            //notificationUi.show(position);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
            musicService.onCompleted();
        }
    }

    public void nextBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            //shuffle & repeat
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomMusic(listSongs.size());
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = (position + 1) % listSongs.size();
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        duration_played.setText(formatTime.format(currentPosition));
                    }
                }
            }, 1000);
           // notificationUi.show(position);
            musicService.notificationUi.show(position);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.onCompleted();
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            //shuffle & repeat
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomMusic(listSongs.size());
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = (position + 1) % listSongs.size();
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        duration_played.setText(formatTime.format(currentPosition));
                    }
                }
            }, 1000);
            musicService.notificationUi.show(position);
            //notificationUi.show(position);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    public void playPauseBtnClicked() {
        if (musicService.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.ic_play);
            notificationUi.show(position);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        duration_played.setText(formatTime.format(currentPosition));
                    }
                }
            }, 1000);
        }
        //play
        else {
            musicService.notificationUi.show(position);
           // notificationUi.show(position);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(() -> {
                {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        duration_played.setText(formatTime.format(currentPosition));
                    }
                }
            }, 1000);
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        listSongs = "albumDetails".equals(sender) ? albumFiles : musicFiles;

        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        notificationUi.show(position);
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);
    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlay);
        duration_total = findViewById(R.id.durationTotal);
        seekBar = findViewById(R.id.seekBar);
        playPauseBtn = findViewById(R.id.play_pause);
        cover_art = findViewById(R.id.music_art);
        nextBtn = findViewById(R.id.id_next);
        // backBtn = findViewById(R.id.back_btn);
        prevBtn = findViewById(R.id.id_prev);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);
    }

    private void updateDuration() {
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        duration_total.setText(formatTime.format(durationTotal));
    }

    private void metaData(Uri uri) {
        byte[] art = bytesFromUri.albumArt(uri.toString());
        updateDuration();
        //получаем картинку , если картинки нет используем стандартную
        if (art != null)
            Glide.with(this).asBitmap().load(art).into(cover_art);
         else
            Glide.with(this).load(R.drawable.eminem_kamikaze).into(cover_art);
    }

    private int getRandomMusic(int i) {
        return new Random().nextInt(i);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        Toast.makeText(musicService, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        musicService.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}
//    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap){
//        Animation animationOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
//        Animation animationIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
//        animationOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Glide.with(context).load(bitmap).into(imageView);
//                animationIn.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                imageView.startAnimation(animationIn);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        imageView.startAnimation(animationOut);
//    }