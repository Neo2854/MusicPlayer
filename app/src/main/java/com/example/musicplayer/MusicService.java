package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener{

    private int position;

    private ArrayList<Long> songIDsList;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    //Initialization
    private void Initialize(){
        mediaPlayer = new MediaPlayer();
        position    = 0;

        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioAttributes(new AudioAttributes
                                            .Builder()
                                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                            .build());

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    //copying songs via IDs into songIDsList
    public void setSongIDs(ArrayList<Long> songIDsList){
        this.songIDsList = songIDsList;
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }
}
