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
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import org.jetbrains.annotations.NotNull;

public class NormalPlayerFragment extends Fragment {
    private static final String TAG = "NormalPlayerFragment";

    private ButtonPanelFragment buttonPanelFragment;
    private PlaylistFragment playlistFragment;
    private VideoViewFragment videoViewFragment;
    private ProgressBarFragment progressBarFragment;
    private PlaylistViewModel playlistViewModel;

    public NormalPlayerFragment(){}

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
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
            buttonPanelFragment = (ButtonPanelFragment) getChildFragmentManager().findFragmentById(R.id.button_panel);
            videoViewFragment = (VideoViewFragment) getChildFragmentManager().findFragmentById(R.id.video_layout);
            playlistFragment = (PlaylistFragment) getChildFragmentManager().findFragmentById(R.id.playlist_view);
            progressBarFragment = (ProgressBarFragment) getChildFragmentManager().findFragmentById(R.id.progress_bar);
        }

        return view;
    }
}
