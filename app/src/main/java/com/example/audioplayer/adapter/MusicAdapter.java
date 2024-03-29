package com.example.audioplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.audioplayer.PlayerActivity;
import com.example.audioplayer.R;
import com.example.audioplayer.model.MusicFiles;
import com.example.audioplayer.utils.BytesFromUri;

import java.util.ArrayList;

import static com.example.audioplayer.MainActivity.albums;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private final ArrayList<MusicFiles> musicFiles;
    private final BytesFromUri bytesFromUri = new BytesFromUri.Base();


    public MusicAdapter( ArrayList<MusicFiles> musicFiles){
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText(musicFiles.get(position).getTitle());
        byte [] image = bytesFromUri.albumArt(musicFiles.get(position).getPath());
        if (image == null) {
            Glide.with(holder.album_art.getContext()).load(R.drawable.eminem_kamikaze).into(holder.album_art);       //Если нет то берем картинку
        } else {
            Glide.with(holder.album_art.getContext()).asBitmap().load(image).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext() , PlayerActivity.class);
            intent.putExtra("position", position);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
        }
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<String> duplicate =new ArrayList<>();
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
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);

                Log.e("PATH:" + path, "Albums:" + album);
                audioList.add(musicFiles);
                if(!duplicate.contains(album)) {
                    albums.add(musicFiles);
                    duplicate.add(album);
                }
            }
            cursor.close();
        }
        return audioList;
    }
}
