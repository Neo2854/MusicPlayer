package com.example.musicplayer;

import android.provider.MediaStore;

import java.util.HashMap;

public class LocalDatabase {
    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int ALBUM_ID = 2;
    public static final int ALBUM = 3;
    public static final int ARTIST = 4;
    public static final int DURATION = 5;

    public static final String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
    };

    public static final String selection = "IS_MUSIC != 0 and DURATION > 50000";

    public static HashMap<Integer,SongSet> albumMap;

    public static SongSet allSongsSet;
}
