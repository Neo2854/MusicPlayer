package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MenuPagerAdapter extends FragmentPagerAdapter {

    private final int menuItemsCount = 4;

    public MenuPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AlbumFragment();
            case 1:
                return new SongsFragment();
            case 2:
                return new PlaylistFragment();
            case 3:
                return new ArtistFragment();
            default:
                return new ArtistFragment();
        }
    }

    @Override
    public int getCount() {
        return menuItemsCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        /*
        switch (position){
            case 0:
                return "Playlists";
            case 1:
                return "Songs";
            case 2:
                return "Albums";
            default:


                return null;
        }
        */
         return null;
    }
}
