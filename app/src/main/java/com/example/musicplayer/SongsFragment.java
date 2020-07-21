package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongsFragment extends Fragment {
    //Variables

    //Views
    private View fragmentView;
    private RecyclerView songsRecyclerView;
    public static SongsRecyclerAdapter songsRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.songs_fragment,container,false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Initialize();
        createSongsList();
        populateSongs();
        setRecyclerViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void Initialize(){
        songsRecyclerView = fragmentView.findViewById(R.id.songsRecyclerView);
        songsRecyclerView.setHasFixedSize(true);

    }

    private void createSongsList(){
        LocalDatabase.allSongsSet.sort();
    }

    private void populateSongs(){
        songsRecyclerAdapter = new SongsRecyclerAdapter(getContext(),R.layout.songs_cardview,LocalDatabase.allSongsSet);
        songsRecyclerView.setAdapter(songsRecyclerAdapter);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setRecyclerViewListener(){
        songsRecyclerAdapter.setOnItemClickListener(new SongsRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent playerActivityIntent = new Intent(getContext(),Player.class);
                if(MusicService.playType != MusicService.songs){
                    MusicService.playType = MusicService.songs;
                    MusicService.songsSet = LocalDatabase.allSongsSet;
                }

                MusicService.songPosition = position;
                startActivity(playerActivityIntent);
            }
        });
    }

}
