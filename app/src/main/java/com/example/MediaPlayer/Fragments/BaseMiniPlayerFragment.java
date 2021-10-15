package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseMiniPlayerFragment extends BaseButtonPanelFragment{
    private static final String TAG = "BaseMiniPlayerFragment";

    private TextView media_name;
    private TextView media_artist_duration;
    private MediaEntry mediaEntry;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        PlaylistViewModel playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setMedia(integer);
                media_name.setText(getMedia().getMediaName());
                String text = getMedia().getArtistName() + " - " + convertTime(getMedia().getDuration());
                media_artist_duration.setText(text);
            }
        });
    }

    public void setMedia(int index){
        Log.d(TAG, "setMedia: ");
        mediaEntry = getMediaList().get(index);
    }

    public MediaEntry getMedia(){
        return mediaEntry;
    }

    public List<MediaEntry> getMediaList(){
        return null;
    }

    @Override
    public void setId(View view) {
        Log.d(TAG, "setId: ");
        Log.d(TAG, "setId: " + super.getLayout());
        super.setNextButton(R.id.next_mini_player, view);
        super.setPauseButton(R.id.pause_mini_video_player, view);
        media_artist_duration = view.findViewById(R.id.artist_and_duration);
        media_name = view.findViewById(R.id.media_name_mini_song_player);
    }

    @Override
    public void setUpButtonListener() {
        super.setupPauseButtonListener();
        super.setupNextButtonListener();
    }

    @Override
    public void setUpViewModel() {
        Log.d(TAG, "setUpViewModel:");
        super.setUpViewModel();
        super.observePause();
        super.observeShuffle();
    }

    public String convertTime(int duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));
    }
}
