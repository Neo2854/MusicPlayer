package com.example.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.ArraySet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Set;

import static com.example.musicplayer.App.MUSCI_CHANNEL_ID;

public class MusicService extends Service {
    public static final int PLAY_SONG  = 0;
    public static final int PAUSE_N_SONG = 1;

    public static ArrayList<Song> songsList = new ArrayList<>();

    public static int position;
    public static int songCurrPosition;
    //Variables
    public static boolean firstLaunch = true;
    public static boolean isPlaying = false;
    public static boolean isPaused;

    private MediaPlayer mediaPlayer;

    private Song song;

    private Uri trackUri;

    private Notification musicNotification;

    @Override
    public void onCreate() {
        super.onCreate();

        Initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeCommand(intent.getIntExtra("command",-1));

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void Initialize(){
        songCurrPosition = -1;
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }



    private void buildNotification(){
        Intent intent = new Intent(this,Player.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                0
        );

        musicNotification = new NotificationCompat.Builder(this,MUSCI_CHANNEL_ID)
                                                .setContentTitle(song.getTitle())
                                                .setContentText(song.getArtist())
                                                .setSmallIcon(R.drawable.play_icon)
                                                .setContentIntent(pendingIntent)
                                                .build();



    }

    private void setSong(){
        trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,song.getId());

        try{
            mediaPlayer.setDataSource(this,trackUri);
        }
        catch (Exception e){
            Log.d("MEDIA PLAYER","Set Data Resource Error");
        }

    }

    private void playSong(){
        mediaPlayer.prepareAsync();
    }

    private void startAll(){
        song = songsList.get(position);

        setSong();
        buildNotification();
        playSong();
        startForeground(1,musicNotification);
    }

    private void executeCommand(int command){
        switch (command){
            case PLAY_SONG:
                if(mediaPlayer.isPlaying()){
                    if(trackUri != ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            songsList.get(position).getId())){
                        mediaPlayer.reset();
                        startAll();
                    }
                }
                else {
                    if(isPaused){
                        mediaPlayer.reset();
                    }
                    startAll();
                }
                break;
            case PAUSE_N_SONG:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    isPaused = true;
                }
                else {
                    mediaPlayer.start();
                }
                break;
        }
    }
}
