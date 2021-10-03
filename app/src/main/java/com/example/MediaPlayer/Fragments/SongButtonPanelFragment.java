package com.example.MediaPlayer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
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
    public int getNextButton() {
        return R.id.next_song;
    }

    @Override
    public int getPrevButton() {
        return R.id.prev_song;
    }

    @Override
    public int getPauseButton() {
        return R.id.pause_song;
    }

    @Override
    public int getShuffleButton() {
        return R.id.shuffle_song;
    }

    @Override
    public int getRepeatButton() {
        return R.id.repeat_song;
    }

    @Override
    public int getButtonPanelLayout() {
        return R.id.button_panel_layout;
    }
}
