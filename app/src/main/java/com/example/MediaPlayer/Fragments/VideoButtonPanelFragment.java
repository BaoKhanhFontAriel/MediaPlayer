package com.example.MediaPlayer.Fragments;

import android.util.Log;
import android.view.View;

import com.example.MediaPlayer.R;

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
        super.setPauseButton(R.id.pause_mini_video_player, view);
        super.setNextButton(R.id.next_mini_player, view);
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
