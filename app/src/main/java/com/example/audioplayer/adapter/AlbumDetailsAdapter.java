package com.example.audioplayer.adapter;

import android.content.Intent;
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

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {

    public static ArrayList<MusicFiles> albumFiles;
    private final BytesFromUri bytesFromUri = new BytesFromUri.Base();

    public AlbumDetailsAdapter(ArrayList<MusicFiles> albumFiles) {
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MusicFiles musicFiles = albumFiles.get(position);
        holder.album_name.setText(musicFiles.getTitle());
        byte[] image = bytesFromUri.albumArt(musicFiles.getPath());
        if (image != null) {
            Glide.with(holder.album_image.getContext()).asBitmap().load(image).into(holder.album_image);
        } else {
            Glide.with(holder.album_image.getContext()).load(R.drawable.eminem_kamikaze).into(holder.album_image);       //Если нет то берем картинку
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PlayerActivity.class);
            intent.putExtra("sender", "albumDetails");
            intent.putExtra("position", position);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final ImageView album_image;
        private final TextView album_name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_file_name);
        }
    }
}
