package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.MediaPlayer.Adapter.AudioPagerAdapter;
import com.example.MediaPlayer.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class AudioTabFragment extends Fragment{
    AudioPagerAdapter audioPagerAdapter;
    private ViewPager2 viewPager2;
    private String[] tabTitle = {"TRACK", "ALBUM", "ARTIST", "GENRE"};

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioPagerAdapter = new AudioPagerAdapter(this);
        viewPager2 = view.findViewById(R.id.pager);
        viewPager2.setAdapter(audioPagerAdapter);
        viewPager2.setSaveEnabled(false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitle[position])
        ).attach();
    }
}
