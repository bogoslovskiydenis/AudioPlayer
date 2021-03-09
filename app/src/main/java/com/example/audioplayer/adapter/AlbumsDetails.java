package com.example.audioplayer.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.audioplayer.R;
import com.example.audioplayer.model.MusicFiles;

import java.util.ArrayList;

import static com.example.audioplayer.MainActivity.musicFiles;

public class AlbumsDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumName;
    ArrayList<MusicFiles> albumsSongs = new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_details);
        recyclerView = findViewById(R.id.recyclerView);
        albumPhoto = findViewById(R.id.albumPhoto);
        albumName = getIntent().getStringExtra("albumName");
        int j=0;
        for (int i=0; i< musicFiles.size(); i++){
            if(albumName.equals(musicFiles.get(i).getAlbum())){
                albumsSongs.add(j, musicFiles.get(i));
                j++;
            }
        }
        byte [] image = getAlbumArt(albumsSongs.get(0).getPath());
        if (image != null){
            Glide.with(this).load(image).into(albumPhoto);
        }
        else {
            Glide.with(this).load(R.drawable.eminem_kamikaze).into(albumPhoto);       //Если нет то берем картинку
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumsSongs.size()<1)){
            albumDetailsAdapter=new AlbumDetailsAdapter(this,albumsSongs);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    RecyclerView.VERTICAL,false));
        }
    }

    private byte [] getAlbumArt (String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}