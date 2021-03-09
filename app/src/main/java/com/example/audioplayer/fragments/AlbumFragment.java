package com.example.audioplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.audioplayer.R;
import com.example.audioplayer.adapter.MusicAdapter;
import com.example.audioplayer.adapter.albumAdapter;

import static com.example.audioplayer.MainActivity.musicFiles;


public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    com.example.audioplayer.adapter.albumAdapter albumAdapter;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(!(musicFiles.size() <1)){
            albumAdapter = new albumAdapter(getContext(), musicFiles);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2 ));
        }
        return view;
    }
}