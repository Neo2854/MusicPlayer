package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistRecyclerAdapter.PlaylistViewHolder> {

    private ArrayList<String> data;

    public PlaylistRecyclerAdapter(ArrayList<String> data){
        this.data = data;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder{
        private ImageView Iv;
        private TextView Tv;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.findViewById(R.id.playlistImage);
            itemView.findViewById(R.id.playlistName);
        }
    }

    @NonNull
    @Override
    public PlaylistRecyclerAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater playlistInflater = LayoutInflater.from(parent.getContext());
        View playlistView = playlistInflater.inflate(R.layout.playlist_gridview,parent,false);
        return new PlaylistViewHolder(playlistView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistRecyclerAdapter.PlaylistViewHolder holder, int position) {
        holder.Tv.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
