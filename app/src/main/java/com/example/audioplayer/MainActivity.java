package com.example.audioplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.audioplayer.adapter.ViewPagerAdapter;
import com.example.audioplayer.fragments.AlbumFragment;
import com.example.audioplayer.fragments.SongFragment;
import com.example.audioplayer.model.MusicFiles;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE =1;
    ArrayList<MusicFiles> musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        
      //  initView();
        
    }

    private void permission() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        else {
            //Toast.makeText(this, "Разрешение получено!", Toast.LENGTH_SHORT).show();

            musicFiles = getAllAudio(this);
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
               // Toast.makeText(this, "Разрешение получено!", Toast.LENGTH_SHORT).show();
                musicFiles = getAllAudio(this);
                initView();
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);


            }
        }
    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.table_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongFragment(), "Song");
        viewPagerAdapter.addFragment(new AlbumFragment(), "Albubs");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static ArrayList<MusicFiles> getAllAudio (Context context){
        ArrayList<MusicFiles> audioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST

        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path, title ,artist, album, duration );

                //log
                Log.e("PATH:"+ path, "Albums:" + album);
                audioList.add(musicFiles);
            }
            cursor.close();
        }
        return audioList;

    }
}