package com.example.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static com.example.musicplayer.App.MUSCI_CHANNEL_ID;

public class MusicService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener{
    //Shared Variables
    public static int songPosition;
    public static ArrayList<Song> songsList;
    //Broadcast actions
    public static final String REQUEST_SONG_STATE = "com.example.musicplayer.request_song_state";
    //Variables
    private boolean isPaused;
    //Media Player
    private MediaPlayer mediaPlayer;
    private long songID;
    //Binder
    private final IBinder musicBinder = new MusicBinder();
    //Broadcast Receiver
    private BroadcastReceiver musicBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case REQUEST_SONG_STATE:
                    Log.d("REQUEST_SONG_STATE","called");
                    if(isPaused){
                        Intent d_intent = new Intent(Player.SONG_PAUSED);
                        sendBroadcast(d_intent);
                    }
                    else {
                        Intent d_intent = new Intent(Player.SONG_RESUMED);
                        sendBroadcast(d_intent);
                    }
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(REQUEST_SONG_STATE);

        registerReceiver(musicBroadcastReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(songID != songsList.get(songPosition).getId()){
            setSong();
            playSong();
            buildNotification();
        }

        return START_STICKY;
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
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicBroadcastReceiver);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        isPaused = false;

        Intent intent = new Intent(Player.SONG_RESUMED);
        sendBroadcast(intent);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    private void setSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        else if(isPaused){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        songID = songsList.get(songPosition).getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songID);

        try {
            mediaPlayer.setDataSource(this,musicUri);
        }
        catch (IOException e){
            Log.d("MEDIA DATA SOURCE",e.getMessage());
        }
    }

    private void playSong(){
        mediaPlayer.prepareAsync();
    }

    private void buildNotification(){
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);

        Intent playerIntent = new Intent(this,Player.class);

        Intent[] intents = new Intent[]{mainIntent,playerIntent};

        PendingIntent notificationIntent = PendingIntent.getActivities(this,0,intents,PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, MUSCI_CHANNEL_ID)
                                            .setContentTitle(songsList.get(songPosition).getTitle())
                                            .setContentText(songsList.get(songPosition).getArtist())
                                            .setSmallIcon(R.drawable.play_icon)
                                            .setContentIntent(notificationIntent)
                                            .build();

        startForeground(1,notification);
    }

    public void playNpause(){
        if(isPaused){
            mediaPlayer.start();
            isPaused = false;
            Intent intent = new Intent(Player.SONG_RESUMED);
            sendBroadcast(intent);
        }
        else {
            mediaPlayer.pause();
            isPaused = true;
            Intent intent = new Intent(Player.SONG_PAUSED);
            sendBroadcast(intent);
        }
    }


}
