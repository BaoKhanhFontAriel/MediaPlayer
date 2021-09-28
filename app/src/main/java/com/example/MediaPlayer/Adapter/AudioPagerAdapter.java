package com.example.MediaPlayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.MediaPlayer.Fragments.AlbumFragment;
import com.example.MediaPlayer.Fragments.ArtistFragment;
import com.example.MediaPlayer.Fragments.GenreFragment;
import com.example.MediaPlayer.Fragments.TrackFragment;

import org.jetbrains.annotations.NotNull;

public class AudioPagerAdapter extends FragmentStateAdapter {

    public AudioPagerAdapter(Fragment fm){
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new AlbumFragment();
            case 2: return new ArtistFragment();
            case 3: return new GenreFragment();
        }
        return new TrackFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
