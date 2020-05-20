package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private final int tabIconsCount = 3;
    private int[] tabIcons = {
            R.drawable.albums_icon,
            R.drawable.songs_icon,
            R.drawable.playlist_icon
    };

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

    }

    private void Initialize(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new MenuPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        tabLayout.setupWithViewPager(viewPager);

        //Setting Tab Icons
        for (int i=0;i<tabIconsCount;i++){
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}
