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
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri albumUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor albumCursor = contentResolver.query(albumUri,null,"is_music != 0 and duration > 50000",null,null);

        if(albumCursor != null &&  albumCursor.moveToFirst()){
            int albumCol = albumCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int alnumName = albumCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                Log.d("ALBUM",Integer.toString(albumCursor.getInt(albumCol)) + "  "+ albumCursor.getString(alnumName));
            }while (albumCursor.moveToNext());
        }
    }

    private void createAlbums(){

    }

    private void populateAlbums(){

    }
}
