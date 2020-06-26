package com.example.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StringSet {
    private ArrayList<String> albumsList;

    public StringSet(){
        albumsList = new ArrayList<>();
    }

    public void add(String album){
        for(String d_album : albumsList){
            if(d_album.equals(album)){
                return;
            }
        }
        albumsList.add(album);
    }

    public String get(int position){
        return albumsList.get(position);
    }

    public int size(){
        return albumsList.size();
    }

    public void sort(){
        Collections.sort(albumsList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
