package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import org.jetbrains.annotations.NotNull;

public class FullscreenVideoPlayerFragment extends Fragment {
    private static final String TAG = "FullscreenFragment";

    private VideoButtonPanelFragment buttonPanelFragment;
    private VideoViewFragment videoPlayerFragment;
    private ProgressBarFragment progressBarFragment;
    private TextView artistName;
    private TextView videoName;
    private LinearLayout video_detail;
    private PlaylistViewModel playlistViewModel;
    Handler handler = new Handler(Looper.getMainLooper());

    public FullscreenVideoPlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_layout, container, false);
        Log.d(TAG, "onCreateView: ");


        artistName = view.findViewById(R.id.artist_name);
        videoName = view.findViewById(R.id.video_name);
        video_detail = view.findViewById(R.id.video_detail);

        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        if (savedInstanceState == null) {
            Log.d(TAG, "create fragment: ");
            buttonPanelFragment = (VideoButtonPanelFragment) getChildFragmentManager().findFragmentById(R.id.button_panel);
            videoPlayerFragment = (VideoViewFragment) getChildFragmentManager().findFragmentById(R.id.video_layout);
            progressBarFragment = (ProgressBarFragment) getChildFragmentManager().findFragmentById(R.id.progress_bar);

            playlistViewModel.getIsVideoClicked().observe(getViewLifecycleOwner(), (isClicked) -> {
                Log.d(TAG, "getIsVideoClicked: " + isClicked);
                handler.removeCallbacks(hide);
                if (isClicked) {
                    if (buttonPanelFragment.isVisible()) {
                        hide();
                    } else {
                        show();
                        handler.postDelayed(hide, 3000);
                    }
                }
            });

            playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), integer -> {
                Log.d(TAG, "getCurrentVideoIndex: ");
                MediaEntry videoEntry = playlistViewModel.getCurrentMediaEntry();
                videoName.setText(videoEntry.getMediaName());
                artistName.setText(videoEntry.getArtistName());
                handler.postDelayed(hide, 3000);
            });
        }

        return view;
    }

    public void show() {
        getChildFragmentManager().beginTransaction()
                .show(buttonPanelFragment)
                .commit();
        getChildFragmentManager().beginTransaction()
                .show(progressBarFragment)
                .commit();
        video_detail.setVisibility(View.VISIBLE);
    }

    public void hide() {
        getChildFragmentManager().beginTransaction()
                .hide(buttonPanelFragment)
                .commit();
        getChildFragmentManager().beginTransaction()
                .hide(progressBarFragment)
                .commit();
        video_detail.setVisibility(View.GONE);
    }

    Runnable hide = () -> hide();

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        handler.removeCallbacks(hide);
        playlistViewModel.getIsVideoClicked().setValue(false);
    }
}
