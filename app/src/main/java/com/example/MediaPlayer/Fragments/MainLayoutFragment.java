package com.example.MediaPlayer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class MainLayoutFragment extends Fragment {
    public static String TAG = "MiniPlayerFragment";

    private VideoViewFragment video_layout;
    private TextView video_name;
    private TextView detail;
    private ImageView pause_button;
    private ImageView next_button;
    private LinearLayout detail_and_video_layout;
    private ConstraintLayout controller_layout;
    private PlaylistViewModel playlistViewModel;
    private VideoView videoView;
    private LinearLayout arrow;
    private TextView artistName;
    private BrowseTabFragment fragmentBrowse;
    private VideoTabFragment videoFragment;
    private AudioTabFragment audioFragment;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler = new Handler(Looper.getMainLooper());

    private static final String KEY_PAUSE = "KEY_PAUSE";

    public MainLayoutFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_app_layout, container, false);
        sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences("pref", MainActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (savedInstanceState == null) {
            fragmentBrowse = new BrowseTabFragment();
            audioFragment = new AudioTabFragment();
            videoFragment = new VideoTabFragment();
            setUpNavigation(view);

//            getChildFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.mini_player_fragment, fragmentBrowse)
//                    .addToBackStack("fragmentBrowse")
//                    .commit();
        }

        setId(view);

        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

//        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                Log.d(TAG, "getCurrentVideoIndex: ");
//                if (integer == -1){
//                    detail_and_video_layout.setVisibility(View.GONE);
//                }
//                else {
////                    detail_and_video_layout.setVisibility(View.VISIBLE);
//                    MediaEntry videoEntry = playlistViewModel.getCurrentMediaEntry();
//                    video_name.setText(videoEntry.getMediaName());
//                    artistName.setText(videoEntry.getArtistName());
//                }
//            }
//        });

//        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), (isSelected) -> {
//            pause_button.setSelected(isSelected);
//            editor.putBoolean(KEY_PAUSE, isSelected);
//            editor.apply();
//        });
//
//        playlistViewModel.getIsVideoClicked().observe(getViewLifecycleOwner(), (isClicked) -> {
//            Log.d(TAG, "getIsVideoClicked: " + isClicked);
//            if (isClicked) {
//                ((MainActivity) getActivity()).enterVideoPlayer();
//            }
//        });

//        pause_button.setOnClickListener(v -> {
//            playlistViewModel.getIsPauseSelected().setValue(!pause_button.isSelected());
//        });

//        detail_and_video_layout.setOnClickListener(v -> {
//            Log.d(TAG, "backToNormalPlayer: ");
//            onBackFromMiniPlayer();
//        });



//        next_button.setOnClickListener(v -> {
//
//            if (playlistViewModel.getIsShuffleSelected().getValue()) {
//                ((MainActivity) getActivity()).playNextShuffleVideo();
//                return;
//            }
//
//            ((MainActivity) getActivity()).playNextVideo();
//        });


        return view;
    }

    public void setUpNavigation(View view){
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(listener);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    public void onBackFromMiniPlayer(){
        Log.d(TAG, "onBackFromMiniPlayer: ");
        getChildFragmentManager().popBackStack("browse", 0);
        ((MainActivity) getActivity()).enterVideoPlayer();
    }

    private NavigationBarView.OnItemSelectedListener listener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    getChildFragmentManager().beginTransaction().replace(R.id.mini_player_fragment, fragmentBrowse).commit();
                    return true;

                case R.id.video:
                    getChildFragmentManager().beginTransaction().replace(R.id.mini_player_fragment, videoFragment).commit();
                    return true;

                case R.id.audio:
                    getChildFragmentManager().beginTransaction().replace(R.id.mini_player_fragment, audioFragment).commit();
                    return true;
            }
            return false;
        }
    };

    public  void enterVideoPlayer(){
        getChildFragmentManager().popBackStack("browse", 0);
        handler.postDelayed(enterVideoPlayer, 30);
    }

    public void switchToFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.mini_player_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setId(View view) {
        video_layout = (VideoViewFragment) getChildFragmentManager().findFragmentById(R.id.video_layout);
        pause_button = view.findViewById(R.id.play_and_pause);
        video_name = view.findViewById(R.id.video_name);
        detail = view.findViewById(R.id.artist_and_duration);
        next_button = view.findViewById(R.id.next);
        detail_and_video_layout = view.findViewById(R.id.detail_and_video_layout);
        controller_layout = view.findViewById(R.id.controller_layout);
        videoView = view.findViewById(R.id.videoView);
        artistName = view.findViewById(R.id.artist_and_duration);
    }

    @Override
    public void onStop() {
        super.onStop();
        playlistViewModel.getIsVideoClicked().setValue(false);
    }

    Runnable enterVideoPlayer = new Runnable() {
        @Override
        public void run() {
            ((MainActivity) getActivity()).enterVideoPlayer();
        }
    };
}
