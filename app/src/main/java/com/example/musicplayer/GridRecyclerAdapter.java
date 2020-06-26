package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridViewHolder> {
    private int layout;

    public GridRecyclerAdapter(int layout){
        this.layout = layout;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder{
        private ImageView art;
        private TextView boldTv;
        private TextView lightTv;
        public GridViewHolder(@NonNull View itemView,int layout) {
            super(itemView);

            switch (layout){
                case R.layout.albums_gridview:
                    art = itemView.findViewById(R.id.albumImage);
                    boldTv = itemView.findViewById(R.id.albumName);
                    lightTv = itemView.findViewById(R.id.albumArtistName);
                    break;
                case R.layout.artist_gridview:
                    art = itemView.findViewById(R.id.artistImage);
                    boldTv = itemView.findViewById(R.id.artistName);
                    break;
                case R.layout.playlist_gridview:

                    break;
            }
        }
    }

    @NonNull
    @Override
    public GridRecyclerAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(this.layout,parent,false);
        return new GridViewHolder(view,layout);
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecyclerAdapter.GridViewHolder holder, int position) {
        switch (this.layout){
            case R.layout.albums_gridview:
                holder.boldTv.setText(LocalDatabase.albumsSet.get(position));
                holder.lightTv.setText(LocalDatabase.albumMap.get(LocalDatabase.albumsSet.get(position)).get(0).getArtist());
                holder.art.setImageResource(R.drawable.reputation);
                break;
            case R.layout.artist_gridview:
                holder.boldTv.setText(LocalDatabase.artistsSet.get(position));
                holder.art.setImageResource(R.drawable.reputation);
                break;
            case R.layout.playlist_gridview:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return LocalDatabase.albumsSet.size();
    }
}
