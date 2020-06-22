package com.example.musicplayer;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;

public class Song {
    private long id;
    private String title;
    private String artist;
    private long duration;

    public Song(long id,String title,String artist,long duration){
        this.id       = id;
        this.title    = title;
        this.artist   = artist;
        this.duration = duration;
    }

    public long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }
}
