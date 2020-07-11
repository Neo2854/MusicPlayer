package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder> {

    private String albumArtUri = "content://media/external/audio/albumart";

    private int layout;

    private SongSet Songs;
    private Context context;
    private onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public SongsRecyclerAdapter(Context context,int layout,SongSet titles){
        this.context = context;
        this.layout = layout;
        this.Songs   = titles;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView songTv;
        private TextView artistTv;
        private ImageView albumIv;
        private ImageButton songMenu;

        public SongViewHolder(@NonNull View itemView,int layout, final onItemClickListener listener) {
            super(itemView);

            switch (layout){
                case R.layout.songs_cardview:
                    songTv   = itemView.findViewById(R.id.sName);
                    artistTv = itemView.findViewById(R.id.aName);
                    albumIv = itemView.findViewById(R.id.albumArt);
                    songMenu = itemView.findViewById(R.id.songsMenu);
                    break;
                case R.layout.queue_cardview:
                    songTv = itemView.findViewById(R.id.queue_cardView_songName);
                    artistTv = itemView.findViewById(R.id.queue_cardView_artistName);
                    songMenu = itemView.findViewById(R.id.queue_cardView_menu);
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
        }
    }

    @NonNull
    @Override
    public SongsRecyclerAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater songInflater = LayoutInflater.from(parent.getContext());
        View songView = songInflater.inflate(layout,parent,false);
        return new SongViewHolder(songView,layout,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongsRecyclerAdapter.SongViewHolder holder, final int position) {
        holder.songTv.setText(Songs.get(position).getTitle());
        holder.artistTv.setText(Songs.get(position).getArtist());

        switch (layout){
            case R.layout.songs_cardview:
                Glide.with(context).load(getAlbumArtUri(position)).placeholder(R.drawable.reputation).into(holder.albumIv);
        }
    }

    @Override
    public int getItemCount() {
        return Songs.size();
    }

    private Uri getAlbumArtUri(int position){
        Uri uri = Uri.parse(albumArtUri);

        return ContentUris.withAppendedId(uri,Songs.get(position).getAlbumID());
    }

}
