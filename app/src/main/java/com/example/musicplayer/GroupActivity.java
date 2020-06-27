package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupActivity extends AppCompatActivity {
    //Shared
    public static SongSet songSet;
    public static int playType;
    //Views
    private ImageView imageView;
    private TextView boldTv;
    private TextView lightTv;
    private ImageButton menuIb;
    private ImageButton playIb;
    private RecyclerView songsRecyclerView;
    private SongsRecyclerAdapter songsRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_fragment);

        Initialize();
        createSongsList();
        populateSongs();
        setPlayButtonListener();
        setRecyclerViewListener();
    }

    private void Initialize(){
        imageView = findViewById(R.id.groupImage);
        boldTv = findViewById(R.id.groupName);
        lightTv = findViewById(R.id.groupSongsNumber);
        menuIb = findViewById(R.id.groupMenu);
        playIb = findViewById(R.id.groupPlayButton);
        songsRecyclerView = findViewById(R.id.groupRecyclerView);

        songsRecyclerView.setHasFixedSize(true);

        String  groupName;
        String songsNum;
        switch (playType){
            case MusicService.album:
                groupName = songSet.get(0).getAlbum();
                break;
            case MusicService.playlist:
                groupName = songSet.get(0).getArtist();
                break;
            case MusicService.artist:
                groupName = songSet.get(0).getArtist();
                break;
            default:
                groupName = "groupName";
                break;
        }
        if(songSet.size() > 1){
            songsNum = String.format("%d Songs",songSet.size());
        }
        else {
            songsNum = String.format("%d Song",1);
        }

        boldTv.setText(groupName);
        lightTv.setText(songsNum);
    }

    private void createSongsList(){
        songSet.sort();
    }

    private void populateSongs(){
        songsRecyclerAdapter = new SongsRecyclerAdapter(songSet,getContentResolver());
        songsRecyclerView.setAdapter(songsRecyclerAdapter);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setPlayButtonListener(){
        playIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicService.songPosition = 0;
                startPlayer();
            }
        });
    }

    private void setRecyclerViewListener(){
        songsRecyclerAdapter.setOnItemClickListener(new SongsRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MusicService.songPosition = position;
                startPlayer();
            }
        });
    }

    private void startPlayer(){
        Intent playerActivityIntent = new Intent(this,Player.class);

        MusicService.playType = playType;
        MusicService.songsSet = songSet;

        startActivity(playerActivityIntent);
    }
}
