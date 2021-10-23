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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;
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
    private MediaPlayerViewModel mediaPlayerViewModel;
    private VideoView videoView;
    private LinearLayout arrow;
    private TextView artistName;
    private BrowseTabFragment fragmentBrowse;
    private VideoTabFragment videoFragment;
    private AudioTabFragment audioFragment;
    private AlbumSongsFragment albumSongsFragment;


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
            miniSongPlayerFragment = new MiniSongPlayerFragment();
            miniVideoPlayerFragment = new MiniVideoPlayerFragment();

            setUpNavigation(view);
//            handler.post(initFragments);
        }
        setId(view);
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getIsSong().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSong) {
                if (isSong) {
                    showMiniSongPlayer();
                } else
                    showMiniVideoPlayer();
            }
        });

        return view;
    }


    Runnable initFragments = new Runnable() {
        @Override
        public void run() {

            albumSongsFragment = new AlbumSongsFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.main_app_layout_fragment, albumSongsFragment)
                    .hide(albumSongsFragment)
                    .commit();
        }
    };


    public void showAlbumSongsFragment() {
        Log.d(TAG, "showAlbumSongsFragment: ");
        albumSongsFragment = new AlbumSongsFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.main_app_layout_fragment, albumSongsFragment)
                .addToBackStack(null)
                .commit();
    }

    private GenrePlaylistFragment genrePlaylistFragment;
    public void showGenreSongsFragment() {
        genrePlaylistFragment = new GenrePlaylistFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.main_app_layout_fragment, genrePlaylistFragment)
                .addToBackStack(null)
                .commit();
    }
    public void hideGenreSongsFragment() {
        getChildFragmentManager().popBackStack();
    }

    public void hideAlbumSongsFragment() {
        getChildFragmentManager().popBackStack();
    }

    private BottomNavigationView bottomNavigationView;

    public void hideNavigationBar() {
        bottomNavigationView.setVisibility(View.GONE);
    }
    public void showNavigationBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void setUpNavigation(View view) {
        bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(listener);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    public void onBackFromMiniPlayer() {
        Log.d(TAG, "onBackFromMiniPlayer: ");
        getChildFragmentManager().popBackStack("browse", 0);
        ((MainActivity) getActivity()).enterVideoPlayer();
    }

    private NavigationBarView.OnItemSelectedListener listener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    hideAllFragment();
                    if (fragmentBrowse.isAdded()) {
                        showFragment(fragmentBrowse);
                    } else {
                        getChildFragmentManager().beginTransaction().add(R.id.main_app_layout_fragment, fragmentBrowse)
                                .commit();
                    }
                    return true;
                case R.id.video:
                    hideAllFragment();
                    if (videoFragment.isAdded()) {
                        showFragment(videoFragment);
                    } else {
                        getChildFragmentManager().beginTransaction().add(R.id.main_app_layout_fragment, videoFragment)
                                .commit();

                    }
                    return true;

                case R.id.audio:
                    hideAllFragment();
                    if (audioFragment.isAdded()) {
                        showFragment(audioFragment);
                    } else {
                        getChildFragmentManager().beginTransaction().add(R.id.main_app_layout_fragment, audioFragment)
                                .commit();

                    }
                    return true;
            }
            return false;
        }
    };

    // hide all fragment except miniVideoPlayerFragment or miniSongPlayerFragment
    public void hideAllFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment frag : fm.getFragments()) {
            ft.hide(frag);
        }
        if (miniVideoPlayerFragment.isAdded()) {
            ft.show(miniVideoPlayerFragment);
        } else if (miniSongPlayerFragment.isAdded()) {
            ft.show(miniSongPlayerFragment);
        }

        ft.commit();
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    public void hideFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .hide(fragment)
                .commit();
    }

    MiniVideoPlayerFragment miniVideoPlayerFragment;

    public void showMiniVideoPlayer() {

        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_app_layout_mini_player_container, miniVideoPlayerFragment)
                .commit();
    }

    MiniSongPlayerFragment miniSongPlayerFragment;

    public void showMiniSongPlayer() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_app_layout_mini_player_container, miniSongPlayerFragment)
                .commit();
    }


    public void enterVideoPlayer() {
        getChildFragmentManager().popBackStack("browse", 0);
        handler.postDelayed(enterVideoPlayer, 30);
    }

    public void switchToFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_app_layout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setId(View view) {
        video_layout = (VideoViewFragment) getChildFragmentManager().findFragmentById(R.id.videoview_mini_song_player);
        pause_button = view.findViewById(R.id.pause_mini_media_player);
        video_name = view.findViewById(R.id.media_name_mini_song_player);
        detail = view.findViewById(R.id.artist_and_duration);
        next_button = view.findViewById(R.id.next_mini_player);
        detail_and_video_layout = view.findViewById(R.id.detail_and_video_layout);
        controller_layout = view.findViewById(R.id.song_controller_layout);
        videoView = view.findViewById(R.id.videoView);
        artistName = view.findViewById(R.id.artist_and_duration);
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayerViewModel.getIsVideoClicked().setValue(false);
    }

    Runnable enterVideoPlayer = new Runnable() {
        @Override
        public void run() {
            ((MainActivity) getActivity()).enterVideoPlayer();
        }
    };
}
