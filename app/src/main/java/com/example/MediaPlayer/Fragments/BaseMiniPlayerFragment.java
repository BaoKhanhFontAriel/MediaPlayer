package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import java.util.List;

public class BaseMiniPlayerFragment extends BaseButtonPanelFragment{
    private static final String TAG = "BaseMiniPlayerFragment";

    private TextView media_name;
    private TextView media_artist_duration;
    private MediaEntry mediaEntry;
    private ImageButton pause_button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        MediaPlayerViewModel mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setMedia(integer);
                media_name.setText(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName());
                String text = mediaPlayerViewModel.getCurrentMediaEntry().getArtistName() + " - " + Utils.convertTime(getMedia().getDuration());
                media_artist_duration.setText(text);
            }
        });

        mediaPlayerViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPause) {
                    pause_button.setSelected(isPause);
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
        super.setPauseButton(R.id.pause_mini_media_player, view);
        media_artist_duration = view.findViewById(R.id.artist_and_duration);
        media_name = view.findViewById(R.id.media_name_mini_song_player);
        pause_button = view.findViewById(R.id.pause_mini_media_player);
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
}
