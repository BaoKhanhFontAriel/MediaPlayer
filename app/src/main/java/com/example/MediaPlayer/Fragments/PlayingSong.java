package com.example.MediaPlayer.Fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;

import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.io.IOException;

public class PlayingSong {
    private static final String TAG = "PlayingSongInstance";

    private PlaylistViewModel playlistViewModel;
    Bundle bundle = new Bundle();
    private MediaPlayer mediaPlayer = new MediaPlayer();;
    Handler handler = new Handler(Looper.getMainLooper());
    public static PlayingSong instance;

    public static PlayingSong getInstance(){
        if (instance == null){
            instance = new PlayingSong();
        }
        return instance;
    }


    public void createNewMediaPlayer(Context context, String uri){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(uri));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
}
