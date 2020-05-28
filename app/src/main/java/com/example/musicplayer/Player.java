package com.example.musicplayer;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Player extends AppCompatActivity {
    //Variables

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        Initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if(MusicService.position >=0 && MusicService.position != MusicService.songCurrPosition){
            MusicService.songCurrPosition = MusicService.position;
            startServiceWithCommand(MusicService.PLAY_SONG);
            MusicService.isPlaying = true;
            pauseBt.setImageResource(R.drawable.pause_icon);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Initializing all Views
    private void Initialize(){
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

        if(MusicService.isPlaying){
            pauseBt.setImageResource(R.drawable.pause_icon);
        }
        else {
            pauseBt.setImageResource(R.drawable.play_icon);
        }

        pauseBt.setOnClickListener(buttonListener);

    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pause:
                    if(MusicService.isPlaying){
                        MusicService.isPlaying = false;
                        pauseBt.setImageResource(R.drawable.play_icon);
                        startServiceWithCommand(MusicService.PAUSE_N_SONG);
                    }
                    else {
                        MusicService.isPlaying = true;
                        pauseBt.setImageResource(R.drawable.pause_icon);
                        startServiceWithCommand(MusicService.PAUSE_N_SONG);
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

    private void startServiceWithCommand(int command){
        Intent intent = new Intent(this,MusicService.class);

        intent.putExtra("command",command);
        ContextCompat.startForegroundService(this,intent);
    }
}
