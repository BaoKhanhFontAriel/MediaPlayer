package com.example.MediaPlayer.Fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.List;

public class MiniVideoPlayerFragment extends BaseMiniPlayerFragment{

    private VideoView videoView;
    PlaylistViewModel playlistViewModel;

    @Override
    public int getLayout() {
        return R.layout.mini_video_player_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView = view.findViewById(R.id.videoview_mini_song_player);
        ConstraintLayout video_mini_player = view.findViewById(R.id.video_controller_layout);


        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                handler.removeCallbacks(updateProgress);
                videoView.setVideoURI(Uri.parse(MiniVideoPlayerFragment.super.getMedia().getUri()));
                videoView.start();
                videoView.seekTo(playlistViewModel.getCurrentProcess().getValue());
                handler.post(updateProgress);
            }
        });

        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                videoView.stopPlayback();
            }
        });

        video_mini_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).enterVideoPlayer();
            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            playlistViewModel.getCurrentProcess().setValue(videoView.getCurrentPosition());
        }
    };



    @Override
    public List<MediaEntry> getMediaList() {
        return VideoRepository.getInstance().getVideoList();
    }
}
