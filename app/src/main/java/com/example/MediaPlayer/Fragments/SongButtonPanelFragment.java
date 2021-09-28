package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.MediaPlayer.R;

public class SongButtonPanelFragment extends ButtonPanelFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_button_panel, container, false);
    }

    @Override
    public void setId(View view) {
        super.setId(view);

    }
}
