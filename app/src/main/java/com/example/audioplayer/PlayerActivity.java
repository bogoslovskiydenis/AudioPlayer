package com.example.audioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.audioplayer.model.MusicFiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.audioplayer.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity {

    TextView song_name, artist_name, duration_played, duration_total;
    SeekBar seekBar;
    ImageView cover_art, nextBtn, backBtn, prevBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formatTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private String formatTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    //geting putExtra from Music Adapter
    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;
        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        //seekbar
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
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
        backBtn = findViewById(R.id.back_btn);
        prevBtn = findViewById(R.id.id_prev);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);
    }
}