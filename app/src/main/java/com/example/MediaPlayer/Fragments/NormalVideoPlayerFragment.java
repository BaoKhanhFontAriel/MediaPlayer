package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

public class NormalVideoPlayerFragment extends Fragment {
    private static final String TAG = "NormalPlayerFragment";

    private VideoButtonPanelFragment buttonPanelFragment;
    private PlaylistFragment playlistFragment;
    private VideoViewFragment videoViewFragment;
    private ProgressBarFragment progressBarFragment;
    private MediaPlayerViewModel mediaPlayerViewModel;

    public NormalVideoPlayerFragment(){}

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.normal_layout, container,false);
        if (savedInstanceState == null) {
            Log.d(TAG, "create fragment: ");
            buttonPanelFragment = (VideoButtonPanelFragment) getChildFragmentManager().findFragmentById(R.id.button_panel);
            videoViewFragment = (VideoViewFragment) getChildFragmentManager().findFragmentById(R.id.videoview_mini_song_player);
            playlistFragment = (PlaylistFragment) getChildFragmentManager().findFragmentById(R.id.playlist_view);
            progressBarFragment = (ProgressBarFragment) getChildFragmentManager().findFragmentById(R.id.progress_bar);
        }

        return view;
    }
}
