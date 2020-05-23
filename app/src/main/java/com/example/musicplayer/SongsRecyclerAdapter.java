package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder> {

    private ArrayList<String> titles;
    private onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public SongsRecyclerAdapter(ArrayList<String> titles){
        this.titles   = titles;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public SongViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);

            tv = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){

                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public SongsRecyclerAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater songInflater = LayoutInflater.from(parent.getContext());
        View songView = songInflater.inflate(R.layout.songs_recyclerview,parent,false);
        return new SongViewHolder(songView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsRecyclerAdapter.SongViewHolder holder, int position) {
        holder.tv.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

}
