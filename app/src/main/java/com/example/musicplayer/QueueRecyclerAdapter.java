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

import org.w3c.dom.Text;

public class QueueRecyclerAdapter extends RecyclerView.Adapter<QueueRecyclerAdapter.QueueViewHolder> {

    private Context context;
    private SongSet songSet;

    public QueueRecyclerAdapter(Context context,SongSet songSet){
        this.context = context;
        this.songSet = songSet;
    }

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.queue_cardview,parent,false);
        return new QueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {
        holder.songTv.setText(songSet.get(position).getTitle());
        holder.artistTv.setText(songSet.get(position).getArtist());

        Glide.with(context).load(getAlbumArtUri(position)).error(R.drawable.reputation).into(holder.albumIv);
    }

    @Override
    public int getItemCount() {
        return songSet.size();
    }

    public static class QueueViewHolder extends RecyclerView.ViewHolder{
        private ImageView albumIv;
        private TextView songTv;
        private TextView artistTv;
        private ImageButton menuBt;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);

            albumIv = itemView.findViewById(R.id.albumArt);
            songTv = itemView.findViewById(R.id.sName);
            artistTv = itemView.findViewById(R.id.aName);
            menuBt = itemView.findViewById(R.id.songsMenu);
        }
    }

    private Uri getAlbumArtUri(int position){
        Uri uri = Uri.parse(LocalDatabase.albumArtUri);

        return ContentUris.withAppendedId(uri,songSet.get(position).getAlbumID());
    }
}
