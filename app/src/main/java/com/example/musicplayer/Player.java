package com.example.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Player extends AppCompatActivity {
    //Variables
    private boolean serviceBound;
    //Views in activity
    private SeekBar playerSb;
    private TextView songTv;
    private TextView comTimeTv;
    private TextView remTimeTv;
    private ImageView songIv;
    private ImageButton pauseBt;
    private ImageButton previousBt;
    private ImageButton nextBt;
    private ImageButton shuffleBt;
    private ImageButton repeatBt;
    private ImageButton collapseBt;
    private ImageButton menuBt;
    private ImageButton favouriteBt;
    //Services
    private MusicService musicService;

    private ServiceConnection musicServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = ((MusicService.MusicBinder) service).getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        Intent intent = new Intent(this,MusicService.class);
        ContextCompat.startForegroundService(this,intent);

        Initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,MusicService.class);
        if(!serviceBound){
            bindService(intent,musicServiceConn,Context.BIND_AUTO_CREATE);
        }

        if(MusicService.isPaused){
            pauseBt.setImageResource(R.drawable.play_icon);
        }
        else{
            pauseBt.setImageResource(R.drawable.pause_icon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(musicServiceConn);
        musicService = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Initializing all Views
    private void Initialize(){
        serviceBound = false;

        playerSb    = findViewById(R.id.seekBar);
        songTv      = findViewById(R.id.songName);
        comTimeTv   = findViewById(R.id.com_time);
        remTimeTv   = findViewById(R.id.rem_time);
        songIv      = findViewById(R.id.imageView);
        pauseBt     = findViewById(R.id.pause);
        previousBt  = findViewById(R.id.previous);
        nextBt      = findViewById(R.id.next);
        shuffleBt   = findViewById(R.id.shuffle);
        repeatBt    = findViewById(R.id.repeat);
        collapseBt  = findViewById(R.id.collapseIcon);
        menuBt      = findViewById(R.id.vertical3Dots);
        favouriteBt = findViewById(R.id.favourite);

        pauseBt.setOnClickListener(buttonListener);
        previousBt.setOnClickListener(buttonListener);
        nextBt.setOnClickListener(buttonListener);
        shuffleBt.setOnClickListener(buttonListener);
        repeatBt.setOnClickListener(buttonListener);
        collapseBt.setOnClickListener(buttonListener);
        menuBt.setOnClickListener(buttonListener);
        favouriteBt.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pause:
                    if(MusicService.isPaused){
                        musicService.resumeSong();
                        pauseBt.setImageResource(R.drawable.pause_icon);
                    }
                    else {
                        musicService.pauseSong();
                        pauseBt.setImageResource(R.drawable.play_icon);
                    }
                    break;
                case R.id.previous:

                    break;
                case R.id.next:

                    break;
                case R.id.shuffle:

                    break;
                case R.id.repeat:

                    break;
                case R.id.collapseIcon:

                    break;
                case R.id.vertical3Dots:

                    break;
                case R.id.favourite:

                    break;
                default:

                    break;
            }
        }
    };
}
