package com.example.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongsFragment extends Fragment {

    private ArrayList<Song> songsList;

    private View fragmentView;
    private RecyclerView songsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.songs_fragment,container,false);

        Initialize();
        createSongsList();

        return fragmentView;
    }

    private void Initialize(){
        songsRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerView);
        songsList         = new ArrayList<>();
    }

    private void createSongsList(){
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = null;

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
                songsList.add(new Song(id,title,artist));
            }while (musicCursor.moveToNext());
        }

        Collections.sort(songsList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

    }
}
