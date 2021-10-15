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
import android.widget.ImageButton;
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
    private ImageButton browse;
    private ImageButton playlist;

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
        browse = view.findViewById(R.id.browse_button);
        playlist = view.findViewById(R.id.playlist_button);

        setPlaylistViewModel();

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                handler.removeCallbacks(watchProgress);
                PlayingSong.getInstance().createNewMediaPlayer(getContext(), audioEntry.getUri());
                handler.post(watchProgress);
            }
        });

        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), (isPaused) -> {
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

        playlistViewModel.getCurrentProcess().observe(getViewLifecycleOwner(), integer -> {
            PlayingSong.getInstance().getMediaPlayer().setOnCompletionListener((MediaPlayer.OnCompletionListener) mp -> {
                Log.d(TAG, "complete: ");
                ((MainActivity) getActivity()).onVideoCompleted();
            });
            bundle.putBoolean("playing", mediaPlayer.isPlaying());
            getParentFragmentManager().setFragmentResult("requestKey", bundle);
        });

        playlistViewModel.getIsDragging().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSeekbarDragging) {
                if (!isSeekbarDragging) {
                    handler.removeCallbacks(watchProgress);
                    PlayingSong.getInstance().getMediaPlayer().seekTo(playlistViewModel.getCurrentProcess().getValue());
                    handler.post(watchProgress);
                }
            }
        });
    }

    Runnable watchProgress = new Runnable() {
        @Override
        public void run() {
            playlistViewModel.getCurrentProcess().setValue(PlayingSong.getInstance().getMediaPlayer().getCurrentPosition());
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
