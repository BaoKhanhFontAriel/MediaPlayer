package com.example.MediaPlayer.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

public class SongPlayerFragment extends Fragment {
    private static final String TAG = "SongPlayerFragment";
    private ImageView thumbnail;
    private TextView song_name;
    private TextView artist;
    private MediaPlayerViewModel mediaPlayerViewModel;
    Bundle bundle = new Bundle();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private SongButtonPanelFragment songButtonPanelFragment;
    private ProgressBarFragment progressBarFragment;
    private PlaylistFragment playlistFragment;
    private ImageButton browse;
    private ImageButton playlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_player_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            Log.d(TAG, "create fragment: ");
            songButtonPanelFragment = (SongButtonPanelFragment) getChildFragmentManager().findFragmentById(R.id.button_panel_layout);
//            playlistFragment = (PlaylistFragment) getChildFragmentManager().findFragmentById(R.id.playlist_view);
            progressBarFragment = (ProgressBarFragment) getChildFragmentManager().findFragmentById(R.id.progress_bar);
        }
        thumbnail = view.findViewById(R.id.audio_thumbnail);
        song_name = view.findViewById(R.id.song_name);
        artist = view.findViewById(R.id.song_artist);
        browse = view.findViewById(R.id.browse_button);
        playlist = view.findViewById(R.id.playlist_button);

        setPlaylistViewModel();

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addPlaylistFragment();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .hide(SongPlayerFragment.this)
                                                        .commit();

            }
        });
    }

    public void setPlaylistViewModel(){
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), integer -> {
            Log.d(TAG, "play audio: ");
            if (integer != -1) {
                MediaEntry audioEntry = mediaPlayerViewModel.getCurrentMediaEntry();
                song_name.setText(audioEntry.getMediaName());
                artist.setText(audioEntry.getArtistName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    thumbnail.setImageBitmap(Utils.getThumbnail(getContext(), Uri.parse(audioEntry.getUri()),  500, 500));
                }

                // clear all prev video process
                handler.removeCallbacks(watchProgress);
                PlayingSong.getInstance().createNewMediaPlayer(getContext(), audioEntry.getUri());
                handler.post(watchProgress);
            }
        });

        mediaPlayerViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), (isPaused) -> {
            if (isPaused) {
                Log.d(TAG, "pause: ");

                PlayingSong.getInstance().getMediaPlayer().pause();
                handler.removeCallbacks(watchProgress);
            } else {
                Log.d(TAG, "play: ");
                PlayingSong.getInstance().getMediaPlayer().start();
                handler.post(watchProgress);
            }
        });

        mediaPlayerViewModel.getCurrentProcess().observe(getViewLifecycleOwner(), integer -> {
            PlayingSong.getInstance().getMediaPlayer().setOnCompletionListener((MediaPlayer.OnCompletionListener) mp -> {
                Log.d(TAG, "complete: ");
                ((MainActivity) getActivity()).onVideoCompleted();
            });
            bundle.putBoolean("playing", mediaPlayer.isPlaying());
            getParentFragmentManager().setFragmentResult("requestKey", bundle);
        });

        mediaPlayerViewModel.getIsDragging().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSeekbarDragging) {
                if (!isSeekbarDragging) {
                    handler.removeCallbacks(watchProgress);
                    PlayingSong.getInstance().getMediaPlayer().seekTo(mediaPlayerViewModel.getCurrentProcess().getValue());
                    handler.post(watchProgress);
                }
            }
        });
    }

    Runnable watchProgress = new Runnable() {
        @Override
        public void run() {
            mediaPlayerViewModel.getCurrentProcess().setValue(PlayingSong.getInstance().getMediaPlayer().getCurrentPosition());
            handler.post(watchProgress);
        }
    };
}
