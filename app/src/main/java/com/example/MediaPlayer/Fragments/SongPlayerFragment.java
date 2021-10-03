package com.example.MediaPlayer.Fragments;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.io.IOException;

public class SongPlayerFragment extends Fragment {
    private static final String TAG = "SongPlayerFragment";
    private ImageView thumbnail;
    private TextView song_name;
    private TextView artist;
    private PlaylistViewModel playlistViewModel;
    Bundle bundle = new Bundle();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private SongButtonPanelFragment songButtonPanelFragment;
    private ProgressBarFragment progressBarFragment;
    private PlaylistFragment playlistFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.audio_player_layout, container, false);
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


        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), integer -> {
            Log.d(TAG, "play audio: ");
            if (integer != -1) {
                MediaEntry audioEntry = playlistViewModel.getCurrentMediaEntry();
                song_name.setText(audioEntry.getMediaName());
                artist.setText(audioEntry.getArtistName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    thumbnail.setImageBitmap(getThumbnail(Uri.parse(audioEntry.getUri())));
                }

                // clear all prev video process
                mediaPlayer.reset();
                handler.removeCallbacks(watchProgress);

                try {
                    mediaPlayer.setDataSource(getContext(), Uri.parse(audioEntry.getUri()));
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(((MainActivity) getActivity()).getSavedVideoProcess(audioEntry.getMediaName()));
                    mediaPlayer.start();
                    handler.post(watchProgress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), (isPaused) -> {
            Log.d(TAG, "isPaused: " + isPaused);
            if (isPaused) {
                Log.d(TAG, "pause: ");
                mediaPlayer.pause();
                handler.removeCallbacks(watchProgress);
            } else {
                Log.d(TAG, "play: ");
                mediaPlayer.start();
                handler.post(watchProgress);
            }
        });

        playlistViewModel.getCurrentProcess().observe(getViewLifecycleOwner(), integer -> {

            // use to receive im
            getParentFragmentManager().setFragmentResultListener(Utils.REQUEST_KEY, this, (FragmentResultListener) (requestKey, bundle) -> {
                if (bundle.getBoolean("is seekbar dragging", false)) {
                    handler.removeCallbacks(watchProgress);
                    mediaPlayer.seekTo(playlistViewModel.getCurrentProcess().getValue());
                    handler.post(watchProgress);
                }
            });

            mediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) mp -> {
                Log.d(TAG, "complete: ");
                ((MainActivity) getActivity()).onVideoCompleted();
            });
            ((MainActivity) getActivity()).saveVideoProcess(integer, playlistViewModel.getCurrentMediaEntry().getMediaName());
            bundle.putBoolean("playing", mediaPlayer.isPlaying());
            getParentFragmentManager().setFragmentResult("requestKey", bundle);
        });
    }

    Runnable watchProgress = new Runnable() {
        @Override
        public void run() {
            playlistViewModel.getCurrentProcess().setValue(mediaPlayer.getCurrentPosition());
            handler.post(watchProgress);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Bitmap getThumbnail(Uri uri) {
        Size mSize = new Size(200, 120);
        CancellationSignal ca = new CancellationSignal();
        Bitmap thumb = null;
        try {
            thumb = getContext().getContentResolver().loadThumbnail(uri, mSize, ca);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }

}
