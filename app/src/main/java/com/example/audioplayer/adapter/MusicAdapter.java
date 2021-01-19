package com.example.audioplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audioplayer.R;
import com.example.audioplayer.model.MusicFiles;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private Context musicContext;
    private ArrayList<MusicFiles> musicFiles;

    MusicAdapter(Context musicContext, ArrayList<MusicFiles> musicFiles){
        this.musicContext = musicContext;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(musicContext).inflate(R.layout.menu_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText(musicFiles.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
        }
    }
}
