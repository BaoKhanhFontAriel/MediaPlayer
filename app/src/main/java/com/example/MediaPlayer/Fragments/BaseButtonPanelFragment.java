package com.example.MediaPlayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

public class BaseButtonPanelFragment extends Fragment {
    private final static String TAG = "ButtonPanelFragment";

    private ImageButton pauseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;
    private ImageButton browseButton;
    private ImageButton playlistButton;
    private ImageButton fullscreenButton;
    private LinearLayout buttonPanelLayout;
    private PlaylistViewModel playlistViewModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String KEY_PLAY_MODE = "KEY_PLAY_MODE";
    private static final String KEY_SHUFFLE = "KEY_SHUFFLE";
    private static final String KEY_REPEAT_BUTTON = "KEY_REPEAT_BUTTON";
    private static final String KEY_FULLSCREEN = "KEY_FULLSCREEN";
    private static final String KEY_PAUSE = "KEY_PAUSE";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Bundle result = new Bundle();
    private int repeatButtonMode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(getLayout(), container, false);
    }

    public int getLayout(){
        Log.d(TAG, "getLayout: ");
        return -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSharedPreferences();
        setId(view);
        setUpViewModel();
        setUpButtonListener();
        getSavedButtonPanel();
    }

    public void setUpButtonListener(){

    }

    public void setupPauseButtonListener(){
        pauseButton.setOnClickListener(v -> {
            pauseButton.setSelected(!pauseButton.isSelected());
            playlistViewModel.getIsPauseSelected().setValue(pauseButton.isSelected());
        });
    }

    public void setupNextButtonListener(){
        nextButton.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: next");
            setUpNextButton();
        });
    }

    public void setupPrevButtonListener(){
        prevButton.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: prev");
            setUpPrevButton();
        });
    }
    public void setupShuffleButtonListener(){
        shuffleButton.setOnClickListener(v -> {
            shuffleButton.setSelected(!shuffleButton.isSelected());
            playlistViewModel.getIsShuffleSelected().setValue(shuffleButton.isSelected());
        });
    }
    public void setupRepeatButtonListener(){
        repeatButton.setOnClickListener(v -> {
            repeatButtonMode++;
            if (repeatButtonMode > 2) {
                repeatButtonMode = 0;
            }
            setRepeatButton(repeatButtonMode);
        });
    }

    public void setUpBrowseButtonListener(){
        browseButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).backToMiniPlayer();
        });
    }

    public void setUpFullscreenButtonListener(){
        fullscreenButton.setOnClickListener(v -> {
            setFullscreenButton();
        });
    }

    public void setSharedPreferences(){
        sharedPreferences = getActivity().getSharedPreferences("pref", ((MainActivity) getActivity()).MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setId(View view) {
        Log.d(TAG, "setId: ");

    }

    public void setButtonPanelLayout(int id, View view){
        buttonPanelLayout = view.findViewById(id);
    }

    public void setFullscreenButton(int id, View view){
        fullscreenButton = view.findViewById(id);
    }

    public void setBrowseButton(int id, View view){
        browseButton = view.findViewById(id);
        browseButton.setEnabled(false);
    }

    public void setNextButton(int id, View view){
        nextButton = view.findViewById(id);
    }

    public void setPrevButton(int id, View view){
        prevButton = view.findViewById(id);
    }
    public void setPauseButton(int id, View view){
        pauseButton = view.findViewById(id);
    }
    public void setShuffleButton(int id, View view){
        shuffleButton = view.findViewById(id);
    }
    public void setRepeatButton(int id, View view){
        repeatButton = view.findViewById(id);
    }


    public void setFullscreenButton() {
        fullscreenButton.setSelected(!fullscreenButton.isSelected());
        Log.d(TAG, "setFullscreenButton: " + fullscreenButton.isSelected());
        if (fullscreenButton.isSelected()) {
            ((MainActivity) getActivity()).enterFullscreen();
        } else {
            ((MainActivity) getActivity()).enterVideoPlayer();
        }
    }

    public void setShuffleButton() {
        Log.d(TAG, "setShuffleButton: ");

        ((MainActivity) getActivity()).generateShufflePlaylist();

        getParentFragmentManager().setFragmentResultListener(Utils.REQUEST_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            // if shuffle mode is selected after repeat mode
            // keep repeat mode
            if (Utils.playMode != Utils.PLAY_MODE.REPEAT_ONE) {
                if (shuffleButton.isSelected()) {
                    Utils.playMode = Utils.PLAY_MODE.SHUFFLE;
                    //   do not play next video if current video is playing or is paused
                    if (result.getBoolean("playing") || pauseButton.isSelected()) {
                        return;
                    }
                    ((MainActivity) getActivity()).playNextShuffleVideo();
                } else {
                    Utils.playMode = Utils.PLAY_MODE.AUTO_NEXT;
                    //do not play next video if current video is playing or is paused
                    if (result.getBoolean("playing") || pauseButton.isSelected()) {
                        return;
                    }
                    ((MainActivity) getActivity()).playNextVideo();
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setRepeatButton(int repeatButtonMode) {
        switch (repeatButtonMode) {
            // none is selected
            case 0:
                Utils.isRepeatEnabled = false;
                if (playlistViewModel.getIsShuffleSelected().getValue()) {
                    Utils.playMode = Utils.PLAY_MODE.SHUFFLE;
                } else {
                    Utils.playMode = Utils.PLAY_MODE.AUTO_NEXT;
                }
                repeatButton.setSelected(false);
                repeatButton.setImageDrawable(((MainActivity) getActivity()).getDrawable(R.drawable.repeat_none));
                return;

            // repeat all is selected
            case 1:
                repeatButton.setSelected(true);
                repeatButton.setImageDrawable(((MainActivity) getActivity()).getDrawable(R.drawable.repeat_button));
                Utils.isRepeatEnabled = true;
                return;

            // repeat one is selected
            case 2:
                Utils.isRepeatEnabled = false;
                Utils.playMode = Utils.PLAY_MODE.REPEAT_ONE;
                repeatButton.setSelected(true);
                repeatButton.setImageDrawable(((MainActivity) getActivity()).getDrawable(R.drawable.repeat_one_button));
        }
    }

    public void setUpPrevButton(){
        if (playlistViewModel.getIsShuffleSelected().getValue()) {
            ((MainActivity) getActivity()).playPrevShuffleVideo();
            return;
        }
        ((MainActivity) getActivity()).playPrevVideo();
    }

    public void setUpNextButton(){
        if (playlistViewModel.getIsShuffleSelected().getValue()) {
            ((MainActivity) getActivity()).playNextShuffleVideo();
            return;
        }
        ((MainActivity) getActivity()).playNextVideo();
    }


    public void setUpViewModel() {
        Log.d(TAG, "setUpViewModel: ");
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
    }

    public void observePause(){
        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), position -> {
            pauseButton.setSelected(false);
        });

    }

    public void observeShuffle(){
        playlistViewModel.getIsShuffleSelected().observe(getViewLifecycleOwner(), (isSelected) -> {
            setShuffleButton();
        });
    }

    public void observeBrowse(){
        playlistViewModel.getIsFolderCreated().observe(getViewLifecycleOwner(), (isCreated) -> {
            browseButton.setEnabled(isCreated);
        });
    }

    public void observeFullscreen(){
        playlistViewModel.getIsPauseSelected().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPaused) {
                pauseButton.setSelected(isPaused);
            }
        });
    }

    public void getSavedButtonPanel() {
        Log.d(TAG, "setUpButtonPanel: ");
        Utils.playMode = Utils.PLAY_MODE.valueOf(sharedPreferences.getString(KEY_PLAY_MODE, "AUTO_NEXT"));

        playlistViewModel.getIsShuffleSelected().setValue(sharedPreferences.getBoolean(KEY_SHUFFLE, false));

        repeatButtonMode = sharedPreferences.getInt(KEY_REPEAT_BUTTON, 0);
//        setRepeatButton(repeatButtonMode);

        playlistViewModel.getIsPauseSelected().setValue(sharedPreferences.getBoolean(KEY_PAUSE, true));

        if (sharedPreferences.getBoolean(KEY_FULLSCREEN, false)) {
            fullscreenButton.performClick();
        }
    }



    public void saveButtonPanel() {
        Log.d(TAG, "saveButtonPanel: ");
        Log.d(TAG, "repeatButtonMode: " + repeatButtonMode);
        editor.putString(KEY_PLAY_MODE, Utils.playMode.toString());
        editor.putBoolean(KEY_SHUFFLE, playlistViewModel.getIsShuffleSelected().getValue());
        editor.putInt(KEY_REPEAT_BUTTON, repeatButtonMode);
        editor.putBoolean(KEY_PAUSE, playlistViewModel.getIsPauseSelected().getValue());
        editor.putBoolean(KEY_FULLSCREEN, fullscreenButton.isSelected());
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
//        getSavedButtonPanel();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
//        saveButtonPanel();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
//        saveButtonPanel();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
