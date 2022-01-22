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
import com.example.audioplayer.R;
import com.example.audioplayer.activity.AlbumsDetails;
import com.example.audioplayer.model.MusicFiles;
import com.example.audioplayer.utils.BytesFromUri;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    private final ArrayList<MusicFiles> albumFiles;
    private final BytesFromUri bytesFromUri = new BytesFromUri.Base();

    public AlbumAdapter( ArrayList<MusicFiles> albumFiles) {
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MusicFiles musicFiles = albumFiles.get(position);
        holder.album_name.setText(musicFiles.getAlbum());
        byte[] image = bytesFromUri.albumArt(musicFiles.getPath());
        if (image == null) {
            Glide.with(holder.album_image.getContext()).load(R.drawable.eminem_kamikaze).into(holder.album_image);
        } else {
            Glide.with(holder.album_image.getContext()).asBitmap().load(image).into(holder.album_image);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AlbumsDetails.class);
            intent.putExtra("albumName", musicFiles.getAlbum());
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
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);
        }
    }
}
