package com.example.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridViewHolder> {

    private String albumArtUri = "content://media/external/audio/albumart";

    private int layout;
    private onItemClickListener listener;
    private Context context;

    public interface onItemClickListener{
        void onItemClick(int position);
        void onPlayClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public GridRecyclerAdapter(Context context, int layout){
        this.context = context;
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
                    lightTv = itemView.findViewById(R.id.artistSongsNumber);
                    playIb = itemView.findViewById(R.id.artistPlayButton);
                    break;
                case R.layout.playlist_gridview:
                    art = itemView.findViewById(R.id.playlistImage);
                    boldTv = itemView.findViewById(R.id.playlistName);
                    lightTv = itemView.findViewById(R.id.playlistSongsNumber);
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
        Song song;
        switch (this.layout){
            case R.layout.albums_gridview:
                song = LocalDatabase.albumMap.get(LocalDatabase.albumsSet.get(position)).get(0);
                holder.boldTv.setText(song.getAlbum());
                holder.lightTv.setText(song.getArtist());
                Glide.with(context).load(getAlbumArtUri(song.getAlbumID())).placeholder(R.drawable.reputation).into(holder.art);
                break;
            case R.layout.artist_gridview:
                song = LocalDatabase.artistMap.get(LocalDatabase.artistsSet.get(position)).get(0);
                holder.boldTv.setText(song.getArtist());
                int songNum = LocalDatabase.artistMap.get(LocalDatabase.artistsSet.get(position)).size();
                String lightText;
                if(songNum > 1){
                    lightText = String.format("%d Songs",songNum);
                }
                else {
                    lightText = String.format("%d Song",songNum);
                }
                holder.lightTv.setText(lightText);
                Glide.with(context).load(getAlbumArtUri(song.getAlbumID())).placeholder(R.drawable.reputation).into(holder.art);
                break;
            case R.layout.playlist_gridview:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return LocalDatabase.albumsSet.size();
    }

    private Uri getAlbumArtUri(int albumID){
        Uri uri = Uri.parse(albumArtUri);

        return ContentUris.withAppendedId(uri,albumID);
    }
}
