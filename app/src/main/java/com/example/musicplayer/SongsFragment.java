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
    private SongsRecyclerAdapter songsRecyclerAdapter;
    //Contents
    private ContentResolver musicResolver;
    private Cursor musicCursor;
    //Intents and Uris
    private Uri musicUri;
    private Intent playerActivityIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.songs_fragment,container,false);

        if(MusicService.songsSet == null){
            MusicService.songsSet = new SongSet();
        }

        Initialize();
        createSongsList();
        populateSongs();
        setRecyclerViewListener();

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicCursor.close();
    }

    private void Initialize(){
        songsRecyclerView = fragmentView.findViewById(R.id.songsRecyclerView);
        songsRecyclerView.setHasFixedSize(true);

        musicResolver = getActivity().getContentResolver();
        musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    private void createSongsList(){
        if(musicResolver != null){
            musicCursor = musicResolver.query(musicUri,null,null,null,null);
        }

        if(musicCursor != null && musicCursor.moveToFirst()){
            int idCol     = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleCol  = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long id       = musicCursor.getLong(idCol);
                String title  = musicCursor.getString(titleCol);
                String artist = musicCursor.getString(artistCol);
                MusicService.songsSet.add(new Song(id,title,artist));
            }while (musicCursor.moveToNext());
        }

        MusicService.songsSet.sort();
    }

    private void populateSongs(){
        songsRecyclerAdapter = new SongsRecyclerAdapter(MusicService.songsSet);
        songsRecyclerView.setAdapter(songsRecyclerAdapter);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setRecyclerViewListener(){
        songsRecyclerAdapter.setOnItemClickListener(new SongsRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playerActivityIntent = new Intent(getContext(),Player.class);
                MusicService.songPosition = position;
                startActivity(playerActivityIntent);
            }
        });
    }

}
