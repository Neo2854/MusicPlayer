package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
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
    //Broadcast actions
    public static final String SONG_RESUMED = "com.example.musicplayer.song_resumed";
    public static final String SONG_PAUSED  = "com.example.musicplayer.song_paused";
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
    //Service Connection
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
    //Broadcast receivers
    private BroadcastReceiver playerBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case SONG_PAUSED:
                    Log.d("SONG_PAUSED","Called");
                    pauseBt.setImageResource(R.drawable.play_icon);
                    break;
                case SONG_RESUMED:
                    Log.d("SONG_RESUMED","Called");
                    pauseBt.setImageResource(R.drawable.pause_icon);
                    break;
                default:

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SONG_PAUSED);
        intentFilter.addAction(SONG_RESUMED);

        registerReceiver(playerBroadcastReceiver,intentFilter);

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
        unregisterReceiver(playerBroadcastReceiver);
    }

    //Initializing all Views
    private void Initialize(){
        serviceBound = false;

        Intent intent = new Intent(MusicService.REQUEST_SONG_STATE);
        sendBroadcast(intent);

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
                    musicService.playNpause();
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
