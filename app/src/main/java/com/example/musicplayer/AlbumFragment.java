package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlbumFragment extends Fragment {
    //Views
    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.album_fragment,container,false);

        Initialize();
        createAlbums();
        populateAlbums();

        return fragmentView;
    }

    private void Initialize(){

    }

    private void createAlbums(){

    }

    private void populateAlbums(){

    }
}
