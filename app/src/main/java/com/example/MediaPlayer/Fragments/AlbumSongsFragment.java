package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.R;

import org.jetbrains.annotations.NotNull;

public class AlbumSongsFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_album_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).showBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        Log.d("TAG", "onOptionsItemSelected: " + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            ((MainActivity)getActivity()).hideBackButton();
            ((MainLayoutFragment) getParentFragment().getParentFragment()).showNavigationBar();
            ((MainLayoutFragment) getParentFragment().getParentFragment()).hideAlbumSongsFragment();
        }
        return true;
    }


}
