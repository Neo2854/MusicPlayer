package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener{

    private int position;

    private ArrayList<Song> songsList;

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
}
