package com.example.musicplayer;

import java.util.ArrayList;

public class FavSet {
    private ArrayList<Long> favList;

    public FavSet(){
        favList = new ArrayList<>();
    }

    public void add(Long songId){
        for(Long d_long : favList){
            if(d_long == songId){
                return;
            }
        }
        favList.add(songId);
    }

    public void remove(Long Id){
        favList.remove(Id);
    }

    public long get(int position){
        return favList.get(position);
    }

    public int size(){
        return favList.size();
    }


}
