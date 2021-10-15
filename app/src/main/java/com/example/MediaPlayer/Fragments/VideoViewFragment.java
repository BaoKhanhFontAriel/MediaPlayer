package com.example.MediaPlayer.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

public class VideoViewFragment extends Fragment {
    public static String TAG = "VideoPlayerFragment";


    private VideoView videoView;
    private RelativeLayout videoLayout;
    private PlaylistViewModel playlistViewModel;
    Bundle bundle = new Bundle();
    Handler handler = new Handler(Looper.getMainLooper());

    public VideoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.video_view, container, false);
        setId(view);
        setUpPlaylistViewModel(videoView);
        return view;
    }

    public void setUpPlaylistViewModel(VideoView videoView){
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        videoView.setOnClickListener(v -> {
            playlistViewModel.getIsVideoClicked().setValue(true);
        });

        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), integer -> {
            Log.d(TAG, "play video: ");
            if (integer != -1) {
                MediaEntry videoEntry = playlistViewModel.getCurrentMediaEntry();
                videoView.setVideoURI(Uri.parse(videoEntry.getUri()));
                videoView.seekTo(((MainActivity) getActivity()).getSavedVideoProcess(videoEntry.getMediaName()));
                videoView.start();
                handler.post(watchProgress);
            }
        });

        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), (isPaused) -> {
            Log.d(TAG, "isPaused: " + isPaused);
            if (isPaused) {
                Log.d(TAG, "pause: ");
                videoView.pause();
                handler.removeCallbacks(watchProgress);
            } else {
                Log.d(TAG, "play: ");
                videoView.start();
                handler.post(watchProgress);
            }
        });

        playlistViewModel.getCurrentProcess().observe(getViewLifecycleOwner(), integer -> {

            videoView.setOnCompletionListener((MediaPlayer.OnCompletionListener) mp -> {
                Log.d(TAG, "complete: ");
                ((MainActivity) getActivity()).onVideoCompleted();
            });
            bundle.putBoolean("playing", videoView.isPlaying());
            getParentFragmentManager().setFragmentResult("requestKey", bundle);
        });

        playlistViewModel.getIsDragging().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSeekbarDragging) {
                if (!isSeekbarDragging) {
                    handler.removeCallbacks(watchProgress);
                    videoView.seekTo(playlistViewModel.getCurrentProcess().getValue());
                    handler.post(watchProgress);
                }
            }
        });
    }

    Runnable watchProgress = new Runnable() {
        @Override
        public void run() {
            playlistViewModel.getCurrentProcess().setValue(videoView.getCurrentPosition());
            handler.post(watchProgress);
        }
    };


    public void setId(View view) {
        videoView = view.findViewById(R.id.videoView);
        videoLayout = view.findViewById(R.id.videoview_mini_song_player);
    }


    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(watchProgress);
    }
}
