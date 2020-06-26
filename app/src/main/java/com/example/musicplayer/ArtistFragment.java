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
        artistRecyclerAdapter = new GridRecyclerAdapter(R.layout.artist_gridview);
        artistRecyclerView.setAdapter(artistRecyclerAdapter);
        artistRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),COL_COUNT));
    }


}
