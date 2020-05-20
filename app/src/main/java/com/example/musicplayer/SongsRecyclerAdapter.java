package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewAdapter> {

    private ArrayList<String> titles;

    public SongsRecyclerAdapter(ArrayList<String> titles){
        this.titles   = titles;
    }

    @NonNull
    @Override
    public SongsRecyclerAdapter.SongViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater songInflater = LayoutInflater.from(parent.getContext());
        View songView = songInflater.inflate(null,parent,false);
        return new SongViewAdapter(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsRecyclerAdapter.SongViewAdapter holder, int position) {
        holder.tv.setText(titles.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class SongViewAdapter extends RecyclerView.ViewHolder {
        private TextView tv;
        public SongViewAdapter(@NonNull View itemView) {
            super(itemView);
        }
    }
}
