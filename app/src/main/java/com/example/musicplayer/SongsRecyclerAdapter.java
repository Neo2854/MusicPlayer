package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder> {

    private SongSet Songs;
    private ContentResolver contentResolver;
    private onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public SongsRecyclerAdapter(SongSet titles,ContentResolver contentResolver){
        this.Songs   = titles;
        this.contentResolver = contentResolver;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView songTv;
        private TextView artistTv;
        private ImageView albumIv;

        public SongViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);

            songTv   = itemView.findViewById(R.id.sName);
            artistTv = itemView.findViewById(R.id.aName);
            albumIv = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }

    }

    @NonNull
    @Override
    public SongsRecyclerAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater songInflater = LayoutInflater.from(parent.getContext());
        View songView = songInflater.inflate(R.layout.songs_cardview,parent,false);
        return new SongViewHolder(songView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsRecyclerAdapter.SongViewHolder holder, int position) {
        holder.songTv.setText(Songs.get(position).getTitle());
        holder.artistTv.setText(Songs.get(position).getArtist());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        thread.start();
    }

    @Override
    public int getItemCount() {
        return Songs.size();
    }

}
