package com.example.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.lang.UProperty;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import static com.example.musicplayer.App.MUSCI_CHANNEL_ID;

public class MusicService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener{
    //Constants
    public static final int album = 0;
    public static final int songs = 1;
    public static final int playlist = 2;
    public static final int artist = 3;

    public static final int normal = 0;
    public static final int shuffle = 1;
    public static final int loop = 2;
    public static final int loop_once = 3;

    private String albumArtUri = "content://media/external/audio/albumart";
    //Broadcast Actions
    public static final String REQUEST_UI = "com.example.musicplayer.musicservice.request_ui";
    public static final String PLAY_N_PAUSE = "com.example.musicplayer.musicservice.play_n_pause";
    public static final String PLAY_NEXT = "com.example.musicplayer.musicservice.play_next";
    public static final String PLAY_PREV = "com.example.musicplayer.musicservice.play_prev";

    private String[] actions = {
            REQUEST_UI,
            PLAY_N_PAUSE,
            PLAY_NEXT,
            PLAY_PREV
    };
    private boolean isHandlingBroadcasts = false;
    //Broadcast Receiver
    private BroadcastReceiver serviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case REQUEST_UI:
                    updatePlayerUI();
                    break;
                case PLAY_N_PAUSE:
                    playNpause();

                    break;
                case PLAY_NEXT:
                    if(!isSettingSong){
                        isSettingSong = true;
                        playNext();
                    }
                    break;
                case PLAY_PREV:
                    if(!isSettingSong){
                        isSettingSong = true;
                        playPrev();
                    }
                    break;
            }

        }
    };
    //Shared Variables
    public static SongSet songsSet = new SongSet();
    public static FavSet favSet;
    public static int playType;
    public static int playbackMode;
    public static int songPosition;
    public static boolean isOneLoopFinished = false;
    public static boolean isPaused = false;
    public static boolean isServiceStarted = false;

    public static boolean isFavourite = false;
    //Variables
    private boolean isSettingSong = false;
    //player and binder
    private MediaPlayer mediaPlayer;
    private long currSongID = -1;
    private final IBinder musicBinder = new MusicBinder();
    private Notification notification;
    //Intents

    //VIews
    RemoteViews musicNotificationView;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);

        musicNotificationView = new RemoteViews(getPackageName(),R.layout.notification_layout);

        setNotificationListeners();

        IntentFilter intentFilter = new IntentFilter();

        for (int i=0;i<actions.length;i++){
            intentFilter.addAction(actions[i]);
        }

        registerReceiver(serviceBroadcastReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(currSongID != songsSet.get(songPosition).getId()){
            setSong();
            playSong();
            buildNotification();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceBroadcastReceiver);
        super.onDestroy();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        isPaused = false;
        isServiceStarted = true;
        isSettingSong = false;

        isFavourite = false;
        for(int i=0;i<favSet.size();i++){
            if(currSongID == favSet.get(i)){
                isFavourite = true;
                break;
            }
        }

        musicNotificationView.setTextViewText(R.id.notificationSongName,songsSet.get(songPosition).getTitle());
        musicNotificationView.setTextViewText(R.id.notificationArtistName,songsSet.get(songPosition).getArtist());
        musicNotificationView.setImageViewResource(R.id.notificationPlayButton,R.drawable.notification_pause);
        updatePlayerUI();
        buildNotification();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Intent intent = new Intent(Player.UPDATE_SEEK_UI);
        sendBroadcast(intent);
    }

    private void setNotificationListeners(){
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,
                0,
                new Intent(PLAY_N_PAUSE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this,
                0,
                new Intent(PLAY_NEXT),
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this,
                0,
                new Intent(PLAY_PREV),
                PendingIntent.FLAG_UPDATE_CURRENT);

        musicNotificationView.setOnClickPendingIntent(R.id.notificationPreviousButton,prevPendingIntent);
        musicNotificationView.setOnClickPendingIntent(R.id.notificationPlayButton,playPendingIntent);
        musicNotificationView.setOnClickPendingIntent(R.id.notificationNextButton,nextPendingIntent);
    }

    private void updatePlayerUI(){
        Intent intent = new Intent(Player.UPDATE_UI);
        sendBroadcast(intent);
    }

    private void setSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        else if(isPaused){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        currSongID = songsSet.get(songPosition).getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currSongID);

        try {
            mediaPlayer.setDataSource(this,musicUri);
        }
        catch (IOException e){
            Log.d("DATA SOURCE",e.getMessage());
        }
    }

    private void playSong(){
        mediaPlayer.prepareAsync();
    }

    private void buildNotification(){
        Intent playerIntent = new Intent(this,Player.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(playerIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);


        notification = new NotificationCompat.Builder(this,MUSCI_CHANNEL_ID)
                .setSmallIcon(R.drawable.play_icon)
                .setCustomContentView(musicNotificationView)
                .setContentIntent(pendingIntent)

                .build();

        NotificationTarget notificationTarget = new NotificationTarget(this,
                R.id.notificationImage,
                musicNotificationView,
                notification,
                1);

        Glide.with(this)
                .asBitmap()
                .load(getAlbumArtUri(songPosition))
                .into(notificationTarget);

        startForeground(1,notification);
    }

    private Uri getAlbumArtUri(int position){
        Uri uri = Uri.parse(albumArtUri);

        return ContentUris.withAppendedId(uri,songsSet.get(position).getAlbumID());
    }

    public int getSongDuration(){
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int progress){
        mediaPlayer.seekTo(progress);
    }

    public void playNpause(){
        if(isPaused){
            mediaPlayer.start();
            isPaused = false;
            musicNotificationView.setImageViewResource(R.id.notificationPlayButton,R.drawable.notification_pause);
        }
        else {
            mediaPlayer.pause();
            isPaused  = true;
            musicNotificationView.setImageViewResource(R.id.notificationPlayButton,R.drawable.notification_play);
        }

        Intent intent = new Intent(Player.UPDATE_PAUSE_UI);
        sendBroadcast(intent);

        buildNotification();
    }

    public void playPrev(){
        switch (playbackMode){
            case shuffle:
                int position = new Random().nextInt(songsSet.size());
                if(songPosition == position){
                    songPosition++;
                }
                else {
                    songPosition = position;
                }
                break;
            case loop:
                songPosition--;
                if(songPosition < 0){
                    songPosition = songsSet.size() - 1;
                }
                break;
            default:
                if(songPosition > 0){
                    songPosition--;
                }
                break;
        }

        setSong();
        playSong();
    }

    public void playNext(){
        switch (playbackMode){
            case normal:
                if(songPosition < songsSet.size()-1){
                    songPosition++;
                }
                break;
            case shuffle:
                int position = new Random().nextInt(songsSet.size());
                if(songPosition == position){
                    songPosition++;
                }
                else {
                    songPosition = position;
                }
                break;
            case loop:
                songPosition++;
                if(songPosition > songsSet.size() - 1){
                    songPosition = 0;
                }
                break;
            case loop_once:
                songPosition++;
                if(songPosition > songsSet.size() - 1 && !isOneLoopFinished){
                    songPosition = 0;
                    isOneLoopFinished = true;
                }
                break;
        }

        setSong();
        playSong();
    }

    public void toggleFavourite(){
        if(isFavourite){
            isFavourite = false;
            favSet.remove(currSongID);
        }
        else {
            isFavourite = true;
            favSet.add(currSongID);
        }

        Intent intent = new Intent(Player.UPDATE_FAVOURITE);
        sendBroadcast(intent);
    }
}
