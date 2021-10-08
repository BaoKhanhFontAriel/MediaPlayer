package com.example.MediaPlayer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

public class VideoButtonPanelFragment extends BaseButtonPanelFragment {
    private final static String TAG = "ButtonPanelFragment";


    @Override
    public int getLayout() {
        return R.layout.panel_bar_layout;
    }

    @Override
    public void setId(View view) {
        Log.d(TAG, "setId: ");
        super.setButtonPanelLayout(R.id.button_panel_layout, view);
        super.setPauseButton(R.id.play_and_pause, view);
        super.setNextButton(R.id.next, view);
        super.setPrevButton(R.id.previous, view);
        super.setShuffleButton(R.id.shuffle, view);
        super.setRepeatButton(R.id.repeat, view);
        super.setBrowseButton(R.id.browse_music, view);
        super.setFullscreenButton(R.id.fullscreen, view);
    }

    @Override
    public void setUpButtonListener() {
        super.setupPauseButtonListener();
        super.setupPrevButtonListener();
        super.setupNextButtonListener();
        super.setupShuffleButtonListener();
        super.setupRepeatButtonListener();
        super.setUpFullscreenButtonListener();
        super.setUpBrowseButtonListener();
    }

    @Override
    public void setUpViewModel() {
        super.setUpViewModel();
        super.observePause();
        super.observeFullscreen();
        super.observeShuffle();
        super.observeBrowse();
    }
}
