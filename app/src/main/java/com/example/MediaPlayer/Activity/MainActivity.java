package com.example.MediaPlayer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Data.AlbumRepository;
import com.example.MediaPlayer.Data.AudioArtistRepository;
import com.example.MediaPlayer.Data.AudioRepository;
import com.example.MediaPlayer.Data.FolderRepository;
import com.example.MediaPlayer.Data.GenreRepository;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.Data.ParcelableVideoList;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoHistoryRepository;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Fragments.FullscreenPlayerFragment;
import com.example.MediaPlayer.Fragments.MiniPlayerFragment;
import com.example.MediaPlayer.Fragments.NormalPlayerFragment;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.Service.MyService;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivityC";


    private FullscreenPlayerFragment fullscreenPlayerFragment;
    private MiniPlayerFragment miniPlayerFragment;
    private NormalPlayerFragment normalPlayerFragment;
    private Intent backgroundIntent;

    private PlaylistViewModel playlistViewModel;

    public static final int PERMISSION_READ = 0;
    Handler handler = new Handler(Looper.getMainLooper());

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String KEY_POSITION = "KEY_POSITION";
    private static final String KEY_PROCESS = "KEY_PROCESS";
    private static final String KEY_PLAYLIST = "KEY_PLAYLIST";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRepository();

        IntentFilter intentFilter = new IntentFilter("com.example.MediaPlayer.MyService");
        registerReceiver(broadcastReceiver, intentFilter);

        normalPlayerFragment = new NormalPlayerFragment();
        fullscreenPlayerFragment = new FullscreenPlayerFragment();
        miniPlayerFragment = new MiniPlayerFragment();

        if (savedInstanceState == null) {
            Log.d(TAG, "create fragments: ");
            setId();
            handler.post(makeFolderTree);
        }

        setUpViewModel();
        getSavedData();

        backgroundIntent = new Intent(this, MyService.class);
    }

    private Runnable makeFolderTree = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "makeFolderTree: ");
            FolderRepository.getInstance().makeFolderTree();
            playlistViewModel.getIsFolderCreated().setValue(true);
        }
    };


    public void setUpViewModel() {
        playlistViewModel = new ViewModelProvider(MainActivity.this).get(PlaylistViewModel.class);

        playlistViewModel.getCurrentIndex().observe(this, position -> {
            backgroundIntent.putExtra(Utils.CURRENT_INDEX, position);
            if (VideoHistoryRepository.getInstance().getHistory() != null) {
                VideoHistoryRepository.getInstance().updateHistory(playlistViewModel.getCurrentMediaEntry());
            }
            saveVideoIndex(position);
        });

        playlistViewModel.getCurrentProcess().observe(this, integer ->
        {
            backgroundIntent.putExtra(Utils.CURRENT_PROGRESS, integer);
            saveVideoProcess(integer, playlistViewModel.getCurrentMediaEntry().getMediaName());
        });

        playlistViewModel.getCurrentPlaylist().observe(this, videoEntries -> {
            ParcelableVideoList videoListParcelable = new ParcelableVideoList(videoEntries);
            backgroundIntent.putExtra(Utils.CURRENT_PLAYLIST, videoListParcelable);
        });
    }

    public void getRepository() {
        if (checkPermission()) {
            // set up video repos
            VideoRepository.getInstance().getAllVideos(this);
            AudioRepository.getInstance().getAudioList(this);
            GenreRepository.getInstance().getGenre(this);
            AudioArtistRepository.getInstance().getAllArtists(this);
            AlbumRepository.getInstance().getAllAlbums(this);
            Log.d(TAG, "VideoRepository: " + VideoRepository.getInstance().getVideoList().size());
            Log.d(TAG, "AudioRepository: " + AudioRepository.getInstance().getAudioList().size());
        }
    }

    public void getSavedData() {
        Log.d(TAG, "getSavedData: ");
        VideoHistoryRepository.getInstance().updateHistory(getSavedHistory());
        playlistViewModel.getCurrentPlaylist().setValue(getSavedPlaylist());
        Log.d(TAG, "getCurrentPlaylist: " + playlistViewModel.getCurrentPlaylist().getValue().size());
        playlistViewModel.getCurrentIndex().setValue(getSavedVideoPosition());
        playlistViewModel.getCurrentProcess().setValue(getSavedVideoProcess(playlistViewModel.getCurrentMediaEntry().getMediaName()));
        playlistViewModel.getIsPauseSelected().setValue(true);
    }


    public void onVideoCompleted() {
        Log.d(TAG, "onVideoCompleted: ");
        String name = playlistViewModel.getCurrentMediaEntry().getMediaName();
        saveVideoProcess(0, name);
        switch (Utils.playMode) {
            case AUTO_NEXT:
                playNextVideo();
                break;
            case REPEAT_ONE:
                repeatOneVideo();
                break;
            case SHUFFLE:
                playNextShuffleVideo();
                break;
        }
    }


    public void getVideo(int position) {
        Log.d(TAG, "getVideo: ");
        playlistViewModel.getCurrentPlaylist().setValue(VideoRepository.getInstance().getVideoList());
        playlistViewModel.getCurrentIndex().setValue(position);
    }

    public void getPLayList(ArrayList<Integer> videoPositions) {
        ArrayList<MediaEntry> videosInSelectedFolder = new ArrayList<>();
        for (int position : videoPositions) {
            videosInSelectedFolder.add(VideoRepository.getInstance().getVideoList().get(position));
        }
        playlistViewModel.getCurrentPlaylist().setValue(videosInSelectedFolder);
        playlistViewModel.getCurrentIndex().setValue(0);
    }

    public void playPrevVideo() {
        Log.d(TAG, "playPrevVideo: ");
        int currentPos = playlistViewModel.getCurrentIndex().getValue();
        if (Utils.isRepeatEnabled && currentPos == 0) {
            currentPos = playlistViewModel.getCurrentPlaylist().getValue().size();
        }

        if (currentPos > 0) {
            currentPos -= 1;
            playlistViewModel.getCurrentIndex().setValue(currentPos);
            playlistViewModel.getCurrentProcess().setValue(getSavedVideoProcess(playlistViewModel.getCurrentMediaEntry().getMediaName()));
        }
    }

    public void playNextVideo() {
        Log.d(TAG, "playNextVideo: ");
        int currentPosition = playlistViewModel.getCurrentIndex().getValue();

        if (Utils.isRepeatEnabled && currentPosition == playlistViewModel.getCurrentPlaylist().getValue().size() - 1) {
            currentPosition = -1;
        }

        if (currentPosition < playlistViewModel.getCurrentPlaylist().getValue().size() - 1) {
            currentPosition += 1;
            playlistViewModel.getCurrentIndex().setValue(currentPosition);
            playlistViewModel.getCurrentProcess().setValue(getSavedVideoProcess(playlistViewModel.getCurrentMediaEntry().getMediaName()));
        }
    }

    public void repeatOneVideo() {
        Log.d(TAG, "repeatOneVideo: ");
        playlistViewModel.getCurrentIndex().setValue(playlistViewModel.getCurrentIndex().getValue());
        playlistViewModel.getCurrentProcess().setValue(0);
    }

    public void playNextShuffleVideo() {
        Log.d(TAG, "playNextShuffleVideo: ");
        if (shuffleIndices.size() == 1) {
            currentShuffleIndex = 0;
        } else {

            if (Utils.isRepeatEnabled && currentShuffleIndex == shuffleIndices.size() - 1) {
                currentShuffleIndex = -1;
            }

            if (currentShuffleIndex < shuffleIndices.size() - 1) {
                currentShuffleIndex++;
                playlistViewModel.getCurrentIndex().setValue(shuffleIndices.get(currentShuffleIndex));
            }
        }

        playlistViewModel.getCurrentProcess().setValue(getSavedVideoProcess(playlistViewModel.getCurrentMediaEntry().getMediaName()));
    }

    public void playPrevShuffleVideo() {
        Log.d(TAG, "playPrevShuffleVideo: ");
        if (shuffleIndices.size() == 1) {
            currentShuffleIndex = 0;
        } else {
            if (Utils.isRepeatEnabled && currentShuffleIndex == 0) {
                currentShuffleIndex = shuffleIndices.size();
            }

            if (currentShuffleIndex > 0) {
                currentShuffleIndex--;

                playlistViewModel.getCurrentIndex().setValue(shuffleIndices.get(currentShuffleIndex));
            }
        }

        playlistViewModel.getCurrentProcess().setValue(getSavedVideoProcess(playlistViewModel.getCurrentMediaEntry().getMediaName()));
    }

    // generate a shuffle playlist by shuffling the video index of the normal video list
    // add all the index to the arrayList, start with the index of the video currently playing
    // the shuffle video currently played is at arraylist.get(i),
    // next shuffle video is at arraylist.get(i++)
    // prev shuffle video is at arraylist.get(i--)
    // where arraylist.get(i) = video index and 0 < i < array.length

    private ArrayList<Integer> shuffleIndices;
    private int currentShuffleIndex;

    public void generateShufflePlaylist() {
        Log.d(TAG, "generateShufflePlaylist: ");

        // create new arrayList and start at beginning
        shuffleIndices = new ArrayList<>();
        currentShuffleIndex = 0;

        shuffleIndices.add(playlistViewModel.getCurrentIndex().getValue());

        if (playlistViewModel.getCurrentPlaylist().getValue().size() > 2) {
            shuffleIndices.add(nextShuffleNumber());
            shuffleIndices.addAll(nextShuffleNumbers());
        }

        if (playlistViewModel.getCurrentPlaylist().getValue().size() == 2) {
            shuffleIndices.add(1 - playlistViewModel.getCurrentIndex().getValue());
        }
    }

    public ArrayList<Integer> nextShuffleNumbers() {
        ArrayList<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < playlistViewModel.getCurrentPlaylist().getValue().size(); i++) {
            if (!shuffleIndices.contains(i)) {
                randomList.add(i);
            }
        }
        Collections.shuffle(randomList);
        return randomList;
    }

    public int nextShuffleNumber() {
        int randomNumber = new Random().nextInt(playlistViewModel.getCurrentPlaylist().getValue().size());
        while (shuffleIndices.contains(randomNumber) || randomNumber == shuffleIndices.get(0) + 1) {
            randomNumber = new Random().nextInt(playlistViewModel.getCurrentPlaylist().getValue().size());
        }
        return randomNumber;
    }


    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: ");
//                        setupVideoRecycler(VideoRepository.getInstance().getVideoList());
                    }
                }
            }
        }
    }


    public void setId() {
        Log.d(TAG, "setId: ");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment, miniPlayerFragment)
                .addToBackStack("miniPlayerFragment")
                .commit();
        Log.d(TAG, "getFragments: " + getSupportFragmentManager().getFragments().toString());
    }


    public void backToMiniPlayer() {
        getSupportFragmentManager().popBackStack("miniPlayerFragment", 0);
    }


    public void enterFullscreen() {
        Log.d(TAG, "enterFullscreen: ");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fullscreenPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void enterVideoPlayer() {
        Log.d(TAG, "enterMiniPlayer: ");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, normalPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void saveVideoIndex(int position) {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(KEY_POSITION, position);
        editor.apply();
    }


    public void saveVideoProcess(int process, String name) {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(name, process);
        editor.apply();
    }


    public int getSavedVideoProcess(String name) {
        Log.d(TAG, "getSavedVideoProcess: ");
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        Log.d(TAG, "getSavedVideoProcess: " + sharedPreferences.getInt(name, 0));
        return sharedPreferences.getInt(name, 0);
    }

    public int getSavedVideoPosition() {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_POSITION, 0);
    }

    public List<MediaEntry> getSavedHistory() {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        String json = sharedPreferences.getString("video history", null);
        if (json != null) {
            Log.d(TAG, "json != null: ");
            Type collectionType = new TypeToken<List<MediaEntry>>() {
            }.getType();
            Gson gson = new Gson();
            return gson.fromJson(json, collectionType);
        }
        return null;
    }

    public void savePlaylist(List<MediaEntry> playlist) {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(KEY_PLAYLIST, gson.toJson(playlist));
        editor.apply();
    }

    public List<MediaEntry> getSavedPlaylist() {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_PLAYLIST, null);
        if (json != null) {
            Log.d(TAG, "json != null: ");
            Type collectionType = new TypeToken<List<MediaEntry>>() {
            }.getType();
            Gson gson = new Gson();
            return gson.fromJson(json, collectionType);
        }
        return VideoRepository.getInstance().getVideoList();
    }


    public void saveHistory() {
        Log.d(TAG, "saveHistory: ");
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(VideoHistoryRepository.getInstance().getHistory());
        editor.putString("video history", json);
        editor.apply();
    }

    // receive data from MyService
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            saveVideoProcess(intent.getIntExtra(Utils.CURRENT_PROGRESS, 0), intent.getStringExtra(Utils.CURRENT_VIDEO_NAME));
            ParcelableVideoList parcelableVideoList = intent.getParcelableExtra(Utils.CURRENT_PLAYLIST);
            playlistViewModel.getCurrentPlaylist().setValue(parcelableVideoList.getVideoEntries());
            playlistViewModel.getCurrentIndex().setValue(intent.getIntExtra(Utils.CURRENT_INDEX, 0));
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        stopService(backgroundIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        VideoRepository.getInstance().getVideoList().clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundIntent.putExtra(Utils.IS_PAUSE, playlistViewModel.getIsPauseSelected().getValue());
        backgroundIntent.putExtra(Utils.CURRENT_SHUFFLE_INDICES, shuffleIndices);
        backgroundIntent.putExtra(Utils.CURRENT_SHUFFLE_INDEX, currentShuffleIndex);
        if (!playlistViewModel.getIsPauseSelected().getValue()) {
            handler.postDelayed(() -> startService(backgroundIntent), 5);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        saveHistory();
    }
}