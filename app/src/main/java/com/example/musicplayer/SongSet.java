package com.example.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongSet {
    private ArrayList<Song> songsList;

    public SongSet(){
        songsList = new ArrayList<>();
    }

    public void add(Song song){
        for(Song d_song : songsList){
            if(d_song.getId() == song.getId()){
                return;
            }
        }
        songsList.add(song);
    }

    public Song remove(int position){
        return  songsList.remove(position);
    }

    public boolean remove(Song song){
        return songsList.remove(song);
    }

    public Song get(int position){
        return songsList.get(position);
    }

    public int size(){
        return songsList.size();
    }

    public void sort(){
        Collections.sort(songsList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }
}
