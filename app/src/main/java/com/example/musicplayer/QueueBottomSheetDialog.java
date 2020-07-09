package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QueueBottomSheetDialog extends BottomSheetDialogFragment {
    //Views
    private View fragmentView;
    private RecyclerView queueRecyclerView;
    private QueueRecyclerAdapter queueRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.queue_bottomsheet,container,false);

        Initialize();
        populateSongs();

        return fragmentView;
    }

    private void Initialize(){
        queueRecyclerView = fragmentView.findViewById(R.id.queue_recyclerView);
        queueRecyclerView.setHasFixedSize(true);
    }

    private void populateSongs(){
        SongSet songSet = new SongSet();
        songSet.add(MusicService.songsSet.get(0));
        queueRecyclerAdapter = new QueueRecyclerAdapter(getContext(),songSet);
        queueRecyclerView.setAdapter(queueRecyclerAdapter);
        queueRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
