package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {
    //Broadcast Actions
    public static final String UPDATE_UI = "com.example.musicplayer.player.update_ui";
    public static final String UPDATE_SEEK_UI = "com.example.musicplayer.player.update_seek_ui";
    public static final String UPDATE_PAUSE_UI = "com.example.musicplayer.player.update_pause_ui";
    public static final String UPDATE_FAVOURITE = "com.example.musicplayer.player.update_favourite";

    private String[] actions = {
            UPDATE_UI,
            UPDATE_SEEK_UI,
            UPDATE_PAUSE_UI,
            UPDATE_FAVOURITE
    };
    //Broadcat Receiver
    private BroadcastReceiver playerBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case UPDATE_UI:
                    //Song Details
                    songTv.setText(MusicService.songsSet.get(MusicService.songPosition).getTitle());
                    artistTv.setText(MusicService.songsSet.get(MusicService.songPosition).getArtist());
                    //Shared Prefs
                    if(MusicService.shuffle){
                        shuffleBt.setImageResource(R.drawable.shuffle_icon_on);
                    }
                    else {
                        shuffleBt.setImageResource(R.drawable.shuffle_icon_off);
                    }

                    if(MusicService.isFavourite){
                        favouriteBt.setImageResource(R.drawable.heart_filled_icon);
                    }
                    else {
                        favouriteBt.setImageResource(R.drawable.heart_outline_icon);
                    }

                    //Pause,Play and Seekbar
                    previousBt.setEnabled(true);
                    nextBt.setEnabled(true);
                    int duration = musicService.getSongDuration();
                    if(duration > 0){
                        remTimeTv.setText(formatMillis(duration));
                        playerSb.setMax(duration);
                    }

                    setSongProgress();
                    handlePause();
                    break;
                case UPDATE_SEEK_UI:
                    setSongProgress();
                    if(!MusicService.isPaused){
                        sbHandler.postDelayed(sbRunnable,200);
                    }
                    break;
                case UPDATE_PAUSE_UI:
                    handlePause();
                    break;
                case UPDATE_FAVOURITE:
                    if(MusicService.isFavourite){
                        favouriteBt.setImageResource(R.drawable.heart_filled_icon);
                    }
                    else {
                        favouriteBt.setImageResource(R.drawable.heart_outline_icon);
                    }

                    break;
                default:
            }
        }
    };
    //Shared Prefs Keys
    private String SHUFFLE = "shuffle";
    private String REPEAT  = "repeat";
    private String FAVOURITES = "favourites";
    //Shared Prefs
    private SharedPreferences sharedPreferences;
    private String SHARED_PREFS = "song_shared_prefs";
    //Views in activity
    private SeekBar playerSb;
    private TextView songTv;
    private TextView artistTv;
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
    //Services Handlers Runnables
    private MusicService musicService;
    private boolean serviceBound = false;
    private Handler sbHandler = new Handler();
    private Runnable sbRunnable = new Runnable() {
        @Override
        public void run() {
            setSongProgress();
            sbHandler.postDelayed(sbRunnable,1000);
        }
    };
    //Service Connection
    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
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

        IntentFilter intentFilter = new IntentFilter();
        for(int i=0;i<actions.length;i++){
            intentFilter.addAction(actions[i]);
        }
        registerReceiver(playerBroadcastReceiver,intentFilter);

        loadSharedPrefs();
        Initialize();

        if(MusicService.isServiceStarted){
            Intent requestIntent = new Intent(MusicService.REQUEST_UI);
            sendBroadcast(requestIntent);
        }

        Intent musicIntent = new Intent(this,MusicService.class);
        if(!serviceBound){
            bindService(musicIntent,musicServiceConnection,BIND_AUTO_CREATE);
        }
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
        sbHandler.removeCallbacks(sbRunnable);
        unregisterReceiver(playerBroadcastReceiver);
        saveSharedPrefs();
        unbindService(musicServiceConnection);
        musicService = null;
        super.onDestroy();
    }

    private void loadSharedPrefs(){
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        MusicService.shuffle = sharedPreferences.getBoolean(SHUFFLE,false);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVOURITES,null);
        Type type = new TypeToken<FavSet>(){}.getType();
        MusicService.favSet = gson.fromJson(json,type);
        if(MusicService.favSet == null){
            MusicService.favSet = new FavSet();
        }
    }

    private void saveSharedPrefs(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHUFFLE,MusicService.shuffle);

        Gson gson = new Gson();
        String json = gson.toJson(MusicService.favSet);
        editor.putString(FAVOURITES,json);
        editor.commit();
    }

    //Initializing all Views
    private void Initialize(){

        playerSb    = findViewById(R.id.seekBar);
        songTv      = findViewById(R.id.songName);
        artistTv    = findViewById(R.id.artistName);
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
        //Start service
        Intent serviceIntent = new Intent(this,MusicService.class);
        ContextCompat.startForegroundService(this,serviceIntent);
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
                    previousBt.setEnabled(false);
                    nextBt.setEnabled(false);
                    break;
                case R.id.next:
                    musicService.playNext();
                    previousBt.setEnabled(false);
                    nextBt.setEnabled(false);
                    break;
                case R.id.shuffle:
                    if(MusicService.shuffle){
                        MusicService.shuffle = false;
                        shuffleBt.setImageResource(R.drawable.shuffle_icon_off);
                    }
                    else {
                        MusicService.shuffle = true;
                        shuffleBt.setImageResource(R.drawable.shuffle_icon_on);
                    }
                    break;
                case R.id.repeat:

                    break;
                case R.id.collapseIcon:
                    finish();
                    break;
                case R.id.vertical3Dots:

                    break;
                case R.id.favourite:
                    musicService.toggleFavourite();
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
                sbHandler.removeCallbacks(sbRunnable);
                musicService.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void setSongProgress(){
        int progress = musicService.getCurrentPosition();
        if(progress > 0){
            playerSb.setProgress(progress);
            comTimeTv.setText(formatMillis(progress));
        }
    }

    private void handlePause(){
        if(MusicService.isPaused){
            pauseBt.setImageResource(R.drawable.play_icon);
            sbHandler.removeCallbacks(sbRunnable);
        }
        else {
            pauseBt.setImageResource(R.drawable.pause_icon);
            sbHandler.postDelayed(sbRunnable,200);
        }
    }

    private String formatMillis(int Millis){
        String time;
        time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Millis),
                TimeUnit.MILLISECONDS.toSeconds(Millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Millis)));

        return time;
    }
}
