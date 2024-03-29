package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {
    private static final int COL_COUNT = 2;
    //Views
    private View fragmentView;
    private RecyclerView playlistRecyclerView;
    private GridRecyclerAdapter playlistRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.playlist_fragment,container,false);

        Initialize();
        createPlaylists();
        populatePlaylists();

        return fragmentView;
    }

    private void Initialize(){
        playlistRecyclerView = fragmentView.findViewById(R.id.playlistRecyclerView);
        playlistRecyclerView.setHasFixedSize(true);
    }

    private void createPlaylists(){

    }

    private void populatePlaylists(){
        playlistRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),COL_COUNT));
    }
}
