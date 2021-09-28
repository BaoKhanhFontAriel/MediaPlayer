package com.example.MediaPlayer.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.concurrent.TimeUnit;

public class ProgressBarFragment extends Fragment {

    private static final String TAG = "ProgressBarFragment";

    private SeekBar seekBar;
    private TextView runtime;
    private TextView duration;
    private LinearLayout progressBarLayout;
    private PlaylistViewModel playlistViewModel;
    private boolean isSeekbarDragged = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    Bundle bundle = new Bundle();

    public ProgressBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_bar, container, false);
        setId(view);

        setUpViewModel();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(seekBar.getProgress());
                runtime.setText(convertToMinuteSecond(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekbarDragged = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playlistViewModel.getCurrentProcess().setValue(seekBar.getProgress());
                bundle.putBoolean("is seekbar dragging", true);
                getParentFragmentManager().setFragmentResult(Utils.REQUEST_KEY, bundle);
                isSeekbarDragged = false;
            }
        });

        return view;
    }

    private void setId(View view) {
        seekBar = view.findViewById(R.id.seekBar);
        runtime = view.findViewById(R.id.runtime);
        duration = view.findViewById(R.id.duration);
        progressBarLayout = view.findViewById(R.id.progress_bar);
    }

    public void setUpViewModel() {
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), position -> {
            seekBar.setMax(playlistViewModel.getCurrentMediaEntry().getDuration());
            duration.setText(String.valueOf(convertToMinuteSecond(playlistViewModel.getCurrentMediaEntry().getDuration())));
        });

        playlistViewModel.getCurrentProcess().observe(getViewLifecycleOwner(), integer -> {
            if (!isSeekbarDragged) {
                runtime.setText(convertToMinuteSecond(playlistViewModel.getCurrentProcess().getValue()));
                seekBar.setProgress(playlistViewModel.getCurrentProcess().getValue());
            }
        });


    }

    public String convertToMinuteSecond(int milliseconds) {

        @SuppressLint("DefaultLocale") String timeInMinutes = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
                TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                milliseconds)));
        return timeInMinutes;
    }

    public void displayProgressBar() {
        progressBarLayout.setAlpha(0f);
        progressBarLayout.setVisibility(View.VISIBLE);
        progressBarLayout.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(null);
    }


    Runnable hideProgressBar = new Runnable() {
        @Override
        public void run() {
            hideProgressBar();
        }
    };


    public void hideProgressBar() {
        progressBarLayout.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}