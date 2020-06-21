package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {
    //Broadcast actions
    public static final String UPDATE_PLAYER_UI = "com.example.musicplayer.player.update_player_ui";
    public static final String UPDATE_PLAYER_UI_SONG_STATE = "com.example.musicplayer.player.update_player_ui_song_state";
    //Constants
    private String[] actions = {
            UPDATE_PLAYER_UI,
            UPDATE_PLAYER_UI_SONG_STATE
    };
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
    //Services,Handlers,Runnables
    private MusicService musicService;
    private Handler sbHandler = new Handler();
    private Runnable sbRunnable = new Runnable() {
        @Override
        public void run() {
            int seconds = musicService.getSongProgress();
            if(seconds >= 0){
                playerSb.setProgress(seconds);
                comTimeTv.setText(formatSeconds(seconds));
            }
            sbHandler.postDelayed(sbRunnable,1000);
        }
    };
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
                case UPDATE_PLAYER_UI:
                    String songTitle = intent.getStringExtra(MusicService.SONG_NAME);
                    int songDuration = intent.getIntExtra(MusicService.SONG_DURATION,-1);

                    playerSb.setMax(songDuration);
                    songTv.setText(songTitle);
                    if(songDuration >= 0){
                        remTimeTv.setText(formatSeconds(songDuration));
                    }

                case UPDATE_PLAYER_UI_SONG_STATE:
                    boolean isPaused = intent.getBooleanExtra(MusicService.IS_PAUSED,false);
                    playerSb.setProgress(musicService.getSongProgress());
                    comTimeTv.setText(formatSeconds(musicService.getSongProgress()));
                    if(isPaused){
                        pauseBt.setImageResource(R.drawable.play_icon);
                        sbHandler.removeCallbacks(sbRunnable);
                    }
                    else {
                        pauseBt.setImageResource(R.drawable.pause_icon);
                        sbHandler.postDelayed(sbRunnable,500);
                    }
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
        for (int i=0;i<actions.length;i++){
            intentFilter.addAction(actions[i]);
        }

        registerReceiver(playerBroadcastReceiver,intentFilter);

        Intent intent = new Intent(this,MusicService.class);
        ContextCompat.startForegroundService(this,intent);
        //Initializes UI according to MusicService
        Initialize();

        Intent serviceIntent = new Intent(this,MusicService.class);
        bindService(serviceIntent,musicServiceConn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sbHandler.removeCallbacks(sbRunnable);
        unregisterReceiver(playerBroadcastReceiver);
        unbindService(musicServiceConn);
        musicService = null;
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

        playerSb.setOnSeekBarChangeListener(seekBarChangeListener);

        Intent intent = new Intent(MusicService.REQUEST_UI);
        sendBroadcast(intent);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pause:
                    musicService.playNpause();
                    break;
                case R.id.previous:
                    musicService.playPrev();
                    break;
                case R.id.next:
                    musicService.playNext();
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

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                musicService.seekTo(progress);
                comTimeTv.setText(formatSeconds(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private String formatSeconds(int seconds){
        String time;
        time = String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes(seconds),
                seconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));

        return time;
    }
}
