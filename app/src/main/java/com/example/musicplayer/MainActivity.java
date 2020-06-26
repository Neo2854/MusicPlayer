package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private int[] tabIcons = {
            R.drawable.albums_icon,
            R.drawable.songs_icon,
            R.drawable.music_note_icon,
            R.drawable.playlist_icon,
            R.drawable.artist_icon
    };

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(arePermissionsGranted()){
            createLocalDataBase();
            Initialize();
        }
        else{
            requestStoragePermissions();
        }
    }

    private boolean arePermissionsGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(int i=0;i<PERMISSIONS.length;i++){
                if(checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }
        else {
            return true;
        }
    }

    private void requestStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(PERMISSIONS[0]) &&
                shouldShowRequestPermissionRationale(PERMISSIONS[1])){
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Permission needed to read and display songs")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(PERMISSIONS,STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
            else {
                requestPermissions(PERMISSIONS,STORAGE_PERMISSION_CODE);
            }
        }
    }

    private void createLocalDataBase(){
        ContentResolver contentResolver = getContentResolver();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(mediaUri,LocalDatabase.projection,LocalDatabase.selection,null,null);

        if(cursor.moveToFirst()){
            do {
                Song song = new Song(
                        cursor.getLong(LocalDatabase.ID),
                        cursor.getString(LocalDatabase.TITLE),
                        cursor.getInt(LocalDatabase.ALBUM_ID),
                        cursor.getString(LocalDatabase.ALBUM),
                        cursor.getString(LocalDatabase.ARTIST)
                );
                if(!song.getTitle().matches("^(AUD-.*-WA.*|Rec.*|REC.*)")){
                    LocalDatabase.allSongsSet.add(song);
                    LocalDatabase.albumsSet.add(song.getAlbum());
                    LocalDatabase.artistsSet.add(song.getArtist());

                    if(LocalDatabase.albumMap.get(song.getAlbum()) == null){
                        LocalDatabase.albumMap.put(song.getAlbum(),new SongSet());
                    }
                    LocalDatabase.albumMap.get(song.getAlbum()).add(song);

                    if(LocalDatabase.artistMap.get(song.getArtist()) == null){
                        LocalDatabase.artistMap.put(song.getArtist(),new SongSet());
                    }
                    LocalDatabase.artistMap.get(song.getArtist()).add(song);
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
    }

    private void Initialize(){
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new MenuPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        tabLayout.setupWithViewPager(viewPager);

        //Setting Tab Icons
        for (int i=0;i<tabIcons.length;i++){
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }

        //Setting default page
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
               grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Initialize();
            }
            else {
                Toast.makeText(this,"Permissions are needed",Toast.LENGTH_SHORT).show();
                requestStoragePermissions();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
