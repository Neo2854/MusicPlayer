package com.example.musicplayer;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridViewHolder> {

    public static class GridViewHolder extends RecyclerView.ViewHolder{

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public GridRecyclerAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecyclerAdapter.GridViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
