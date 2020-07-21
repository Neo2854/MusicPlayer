package com.example.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class ShareSong {

    public ShareSong(Context context,Song song){
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                song.getId());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(Intent.createChooser(shareIntent,String.format("Share '%s'",song.getTitle())));
    }
}
