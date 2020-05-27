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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.musicplayer.App.MUSCI_CHANNEL_ID;

public class MusicService extends Service {
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
        if(mediaPlayer.isPlaying()){
            if(trackUri != ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                                        Songs.songsList.get(Songs.position).getId())){
                mediaPlayer.reset();
                startAll();
            }
        }
        else {
            startAll();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void Initialize(){
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
        song = Songs.songsList.get(Songs.position);

        buildNotification();
        setSong();
        playSong();
        startForeground(1,musicNotification);
    }

}
