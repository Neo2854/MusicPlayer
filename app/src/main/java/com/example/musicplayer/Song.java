package com.example.musicplayer;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;

public class Song {
    private long id;
    private String title;
    private int albumID;
    private String album;
    private String artist;
    private String data;

    public Song(long id,String title,int albumID,String album,String artist,String data){
        this.id      = id;
        this.title   = title;
        this.albumID = albumID;
        this.album   = album;
        this.artist  = artist;
        this.data = data;
    }

    public long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getAlbumID() {
        return albumID;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }
}
