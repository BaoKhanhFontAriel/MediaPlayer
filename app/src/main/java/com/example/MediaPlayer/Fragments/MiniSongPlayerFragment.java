package com.example.MediaPlayer.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.AudioRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.List;

public class MiniSongPlayerFragment extends BaseMiniPlayerFragment{
    private static final String TAG = "MiniSongPlayerFragment";
    PlaylistViewModel playlistViewModelMiniSongPlayer;
    private ImageView thumbnail;
    private ConstraintLayout whole_song_mini_player;


    @Override
    public int getLayout() {
        Log.d(TAG, "getLayout: ");
        return R.layout.mini_song_player_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        thumbnail = view.findViewById(R.id.song_thumbnail_mini_song_player);
        whole_song_mini_player = view.findViewById(R.id.song_controller_layout);

        playlistViewModelMiniSongPlayer = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
        playlistViewModelMiniSongPlayer.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onChanged(Integer integer) {
                thumbnail.setImageBitmap(Utils.getThumbnail(
                        Uri.parse(MiniSongPlayerFragment.super.getMedia().getUri()),
                        getContext()));
            }
        });

        playlistViewModelMiniSongPlayer.getIsPauseSelected().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPause) {
                if (isPause) {
                    PlayingSong.getInstance().getMediaPlayer().pause();
                }
                else
                    PlayingSong.getInstance().getMediaPlayer().start();
            }
        });

        whole_song_mini_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).enterSongPlayer();
            }
        });
    }

    @Override
    public List<MediaEntry> getMediaList() {
        Log.d(TAG, "getMediaList: ");
        return AudioRepository.getInstance().getAudioList();
    }
}
