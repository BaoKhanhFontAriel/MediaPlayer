package com.example.MediaPlayer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.R;

public class SongButtonPanelFragment extends BaseButtonPanelFragment {
    private final static String TAG = "SongButtonPanelFragment";

    @Override
    public int getLayout() {
        return R.layout.song_button_panel;
    }

    @Override
    public void setId(View view) {
        Log.d(TAG, "setId: ");
        super.setButtonPanelLayout(R.id.button_panel_layout, view);
        super.setPauseButton(R.id.pause_song, view);
        super.setNextButton(R.id.next_song, view);
        super.setPrevButton(R.id.prev_song, view);
        super.setShuffleButton(R.id.shuffle_song, view);
        super.setRepeatButton(R.id.repeat_song, view);
    }

    @Override
    public void setUpButtonListener() {
        super.setupPauseButtonListener();
        super.setupPrevButtonListener();
        super.setupNextButtonListener();
        super.setupShuffleButtonListener();
        super.setupRepeatButtonListener();
    }

    @Override
    public void setUpViewModel() {
        super.setUpViewModel();
        super.observePause();
        super.observeShuffle();
    }
}
