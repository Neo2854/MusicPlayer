package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {
    //Broadcast Actions
    public static final String UPDATE_UI = "com.example.musicplayer.player.update_ui";
    public static final String UPDATE_SEEK_UI = "com.example.musicplayer.player.update_seek_ui";
    public static final String UPDATE_PAUSE_UI = "com.example.musicplayer.player.update_pause_ui";
    public static final String UPDATE_FAVOURITE = "com.example.musicplayer.player.update_favourite";

    public static final int[] PLAYBACK_ICONS = {
            R.drawable.normal_playback_icon,
            R.drawable.shuffle_icon,
            R.drawable.repeat_icon,
            R.drawable.repeat_once_icon
    };

    private String[] actions = {
            UPDATE_UI,
            UPDATE_SEEK_UI,
            UPDATE_PAUSE_UI,
            UPDATE_FAVOURITE
    };
    //Variables
    private String albumArtUri = "content://media/external/audio/albumart";
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

                    Glide.with(context)
                            .load(getAlbumArtUri(MusicService.songPosition))
                            .placeholder(R.drawable.reputation)
                            .into(songIv);

                    Glide.with(context)
                            .load(getAlbumArtUri(MusicService.songPosition))
                            .transform(new BlurTransformation(30))
                            .placeholder(R.drawable.reputation)
                            .into(backgroundIv);

                    //Shared Prefs
                    playbackBt.setImageResource(PLAYBACK_ICONS[MusicService.playbackMode]);

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
    private String PLAYBACK_MODE = "playback_mode";
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
    private ImageView backgroundIv;
    private ImageButton pauseBt;
    private ImageButton previousBt;
    private ImageButton nextBt;
    private ImageButton playbackBt;
    private ImageButton queueInfoBt;
    private ImageButton addtoPlaylistBt;
    private ImageButton editInfoBt;
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
        MusicService.playbackMode = sharedPreferences.getInt(PLAYBACK_MODE,0);

        if(MusicService.playbackMode >= PLAYBACK_ICONS.length){
            MusicService.playbackMode = 0;
        }

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
        editor.putInt(PLAYBACK_MODE,MusicService.playbackMode);

        Gson gson = new Gson();
        String json = gson.toJson(MusicService.favSet);
        editor.putString(FAVOURITES,json);
        editor.commit();
    }

    //Initializing all Views
    private void Initialize(){

        playerSb    = findViewById(R.id.seekBar);
        songTv      = findViewById(R.id.songName);
        artistTv    = findViewById(R.id.playerArtistName);
        comTimeTv   = findViewById(R.id.com_time);
        remTimeTv   = findViewById(R.id.rem_time);
        songIv      = findViewById(R.id.imageView);
        backgroundIv = findViewById(R.id.LargeImageViewPlayer);
        pauseBt     = findViewById(R.id.pause);
        previousBt  = findViewById(R.id.previous);
        nextBt      = findViewById(R.id.next);
        playbackBt   = findViewById(R.id.playback_mode_button);
        queueInfoBt = findViewById(R.id.queue_info);
        addtoPlaylistBt = findViewById(R.id.add_playlist);
        editInfoBt    = findViewById(R.id.edit_info);
        collapseBt  = findViewById(R.id.collapseIcon);
        menuBt      = findViewById(R.id.vertical3Dots);
        favouriteBt = findViewById(R.id.favourite);

        pauseBt.setOnClickListener(buttonListener);
        previousBt.setOnClickListener(buttonListener);
        nextBt.setOnClickListener(buttonListener);
        playbackBt.setOnClickListener(buttonListener);
        queueInfoBt.setOnClickListener(buttonListener);
        addtoPlaylistBt.setOnClickListener(buttonListener);
        editInfoBt.setOnClickListener(buttonListener);
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
                case R.id.playback_mode_button:
                    MusicService.isOneLoopFinished = false;
                    MusicService.playbackMode++;
                    if(MusicService.playbackMode >= PLAYBACK_ICONS.length){
                        MusicService.playbackMode = 0;
                    }
                    playbackBt.setImageResource(PLAYBACK_ICONS[MusicService.playbackMode]);
                    String toastText;
                    switch (MusicService.playbackMode){
                        case MusicService.normal:
                            toastText = "Playing as Default";
                            break;
                        case MusicService.shuffle:
                            toastText = "Shuffle On";
                            break;
                        case MusicService.loop:
                            toastText = "Repeat On";
                            break;
                        case MusicService.loop_once:
                            toastText = "Repeating Current Song";
                            break;
                        default:
                            toastText = "";
                            break;
                    }
                    Toast.makeText(v.getContext(),toastText,Toast.LENGTH_SHORT).show();
                    break;
                case R.id.queue_info:
                    QueueBottomSheetDialog queueBottomSheetDialog = new QueueBottomSheetDialog();
                    queueBottomSheetDialog.show(getSupportFragmentManager(),"QUEUE BOTTOMSHEET");
                    break;
                case R.id.add_playlist:

                    break;
                case R.id.edit_info:

                    break;
                case R.id.collapseIcon:
                    finish();
                    break;
                case R.id.vertical3Dots:
                    final PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Song song;
                            switch (item.getItemId()){
                                case R.id.delete:
                                    musicService.deleteCurrSong();
                                    break;
                                case R.id.share:
                                    song = MusicService.songsSet.get(MusicService.songPosition);
                                    ShareSong shareSong = new ShareSong(Player.this,song);
                                    break;
                                default:
                                    return false;
                            }
                            return true;
                        }
                    });
                    popupMenu.inflate(R.menu.player_menu);
                    popupMenu.show();
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
            pauseBt.setImageResource(R.drawable.play_selector);
            sbHandler.removeCallbacks(sbRunnable);
        }
        else {
            pauseBt.setImageResource(R.drawable.pause_selector);
            sbHandler.postDelayed(sbRunnable,200);
        }
    }

    private Uri getAlbumArtUri(int position){
        Uri uri = Uri.parse(albumArtUri);

        return ContentUris.withAppendedId(uri,MusicService.songsSet.get(position).getAlbumID());
    }

    private String formatMillis(int Millis){
        String time;
        time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Millis),
                TimeUnit.MILLISECONDS.toSeconds(Millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Millis)));

        return time;
    }
}
