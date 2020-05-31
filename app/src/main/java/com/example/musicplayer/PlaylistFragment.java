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
    //Views
    private View fragmentView;
    private RecyclerView playlistRecyclerView;
    private PlaylistRecyclerAdapter playlistRecyclerAdapter;

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
        ArrayList<String> a = new ArrayList<>();
        a.add("Playlist 1");
        a.add("Playlist 2");
        a.add("Playlist 3");
        a.add("Long named playlist abchbvbudvujwbdb 2");
        a.add("Playlist 4");
        a.add("Long named playlist vbdvuwehiocwiodncnodiwd 4");

        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(a);

    }

    private void populatePlaylists(){
        playlistRecyclerView.setAdapter(playlistRecyclerAdapter);
        playlistRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }
}
