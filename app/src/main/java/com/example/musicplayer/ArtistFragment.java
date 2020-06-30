package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArtistFragment extends Fragment {
    private static final int COL_COUNT = 2;
    //Views
    private View fragmentView;
    private RecyclerView artistRecyclerView;
    private GridRecyclerAdapter artistRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.artist_fragment,container,false);

        Initialize();
        createArtists();
        populateArtists();
        setRecyclerViewListener();

        return fragmentView;
    }

    private void Initialize(){
        artistRecyclerView = fragmentView.findViewById(R.id.artistRecyclerView);
        artistRecyclerView.setHasFixedSize(true);
    }

    private void createArtists(){
        LocalDatabase.artistsSet.sort();
    }

    private void populateArtists(){
        artistRecyclerAdapter = new GridRecyclerAdapter(getContext(),R.layout.artist_gridview);
        artistRecyclerView.setAdapter(artistRecyclerAdapter);
        artistRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),COL_COUNT));
    }

    private void setRecyclerViewListener(){
        artistRecyclerAdapter.setOnItemClickListener(new GridRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroupActivity.playType = MusicService.artist;
                GroupActivity.songSet = LocalDatabase.artistMap.get(LocalDatabase.artistsSet.get(position));

                Intent groupActivityIntent = new Intent(getContext(),GroupActivity.class);
                startActivity(groupActivityIntent);
            }

            @Override
            public void onPlayClick(int position) {
                MusicService.playType = MusicService.artist;

                MusicService.songsSet = LocalDatabase.artistMap.get(LocalDatabase.artistsSet.get(position));
                MusicService.songPosition = 0;

                Intent playerActivityIntent = new Intent(getContext(),Player.class);
                startActivity(playerActivityIntent);
            }
        });
    }


}
