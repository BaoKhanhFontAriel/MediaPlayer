package com.example.MediaPlayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.MediaPlayer.Fragments.AlbumTabFragment;
import com.example.MediaPlayer.Fragments.ArtistTabFragment;
import com.example.MediaPlayer.Fragments.GenreTabFragment;
import com.example.MediaPlayer.Fragments.AudioTrackTabFragment;

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
            case 1: return new AlbumTabFragment();
            case 2: return new ArtistTabFragment();
            case 3: return new GenreTabFragment();
        }
        return new AudioTrackTabFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
