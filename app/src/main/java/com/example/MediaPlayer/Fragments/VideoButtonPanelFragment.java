package com.example.MediaPlayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

public class VideoButtonPanelFragment extends BaseButtonPanelFragment {
    private final static String TAG = "ButtonPanelFragment";

    private ImageButton browseButton;
    private ImageButton fullscreenButton;
    private PlaylistViewModel playlistViewModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String KEY_FULLSCREEN = "KEY_FULLSCREEN";

    public VideoButtonPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayout() {
        return R.layout.panel_bar_layout;
    }

    @Override
    public void setupButtonListener(){
        super.setupButtonListener();
        browseButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).backToMiniPlayer();
        });

        fullscreenButton.setOnClickListener(v -> {
            setFullscreenButton();
        });
    }

    @Override
    public void setId(View view) {
        Log.d(TAG, "setId: ");
        super.setId(view);
        browseButton = view.findViewById(R.id.browse_music);
        fullscreenButton = view.findViewById(R.id.fullscreen);
        browseButton.setEnabled(false);
    }

    @Override
    public int getNextButton() {
        return R.id.next;
    }

    @Override
    public int getPrevButton() {
        return R.id.previous;
    }

    @Override
    public int getPauseButton() {
        return R.id.play_and_pause;
    }

    @Override
    public int getShuffleButton() {
        return R.id.shuffle;
    }

    @Override
    public int getRepeatButton() {
        return R.id.repeat;
    }

    @Override
    public int getButtonPanelLayout() {
        return R.id.button_panel_layout;
    }

    public void setFullscreenButton() {
        fullscreenButton.setSelected(!fullscreenButton.isSelected());
        Log.d(TAG, "setFullscreenButton: " + fullscreenButton.isSelected());
        if (fullscreenButton.isSelected()) {
            ((MainActivity) getActivity()).enterFullscreen();
        } else {
            ((MainActivity) getActivity()).enterVideoPlayer();
        }
    }


    public void setUpViewModel() {
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getIsFolderCreated().observe(getViewLifecycleOwner(), (isCreated) -> {
            browseButton.setEnabled(isCreated);
        });
    }

    @Override
    public void getSavedButtonPanel() {
        super.getSavedButtonPanel();
        if (sharedPreferences.getBoolean(KEY_FULLSCREEN, false)) {
            fullscreenButton.performClick();
        }
    }

    public void saveButtonPanel() {
        Log.d(TAG, "saveButtonPanel: ");
        editor.putBoolean(KEY_FULLSCREEN, fullscreenButton.isSelected());
        editor.apply();
    }
}
