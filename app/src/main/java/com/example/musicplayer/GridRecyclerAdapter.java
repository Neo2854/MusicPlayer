package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridViewHolder> {
    private int layout;
    private onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(int position);
        void onPlayClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public GridRecyclerAdapter(int layout){
        this.layout = layout;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder{
        private ImageView art;
        private TextView boldTv;
        private TextView lightTv;
        private ImageButton playIb;
        public GridViewHolder(@NonNull View itemView,int layout,final onItemClickListener listener) {
            super(itemView);

            switch (layout){
                case R.layout.albums_gridview:
                    art = itemView.findViewById(R.id.albumImage);
                    boldTv = itemView.findViewById(R.id.albumName);
                    lightTv = itemView.findViewById(R.id.albumArtistName);
                    playIb = itemView.findViewById(R.id.albumPlayButton);
                    break;
                case R.layout.artist_gridview:
                    art = itemView.findViewById(R.id.artistImage);
                    boldTv = itemView.findViewById(R.id.artistName);
                    playIb = itemView.findViewById(R.id.artistPlayButton);
                    break;
                case R.layout.playlist_gridview:
                    art = itemView.findViewById(R.id.playlistImage);
                    boldTv = itemView.findViewById(R.id.playlistName);
                    playIb = itemView.findViewById(R.id.playlistPlayButton);
                    break;
            }

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

            playIb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onPlayClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public GridRecyclerAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(this.layout,parent,false);
        return new GridViewHolder(view,layout,listener);
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
