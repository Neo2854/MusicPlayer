package com.example.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumFragment extends Fragment {
    private static final int COL_COUNT = 2;
    //Views
    private View fragmentView;
    private RecyclerView albumsRecyclerView;
    private GridRecyclerAdapter albumsRecyclerAdapter;

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
        albumsRecyclerView = fragmentView.findViewById(R.id.albumRecyclerView);
        albumsRecyclerView.setHasFixedSize(true);
    }

    private void createAlbums(){
        LocalDatabase.albumsSet.sort();
    }

    private void populateAlbums(){
        albumsRecyclerAdapter = new GridRecyclerAdapter(R.layout.albums_gridview);
        albumsRecyclerView.setAdapter(albumsRecyclerAdapter);
        albumsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),COL_COUNT));
    }
}
