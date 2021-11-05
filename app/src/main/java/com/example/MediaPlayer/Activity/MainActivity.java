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
import com.example.MediaPlayer.Data.SongArtistRepository;
import com.example.MediaPlayer.Data.SongRepository;
import com.example.MediaPlayer.Data.FolderRepository;
import com.example.MediaPlayer.Data.GenreRepository;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.Data.ParcelableVideoList;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoHistoryRepository;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Fragments.AlbumSongsFragment;
import com.example.MediaPlayer.Fragments.FullscreenVideoPlayerFragment;
import com.example.MediaPlayer.Fragments.MainLayoutFragment;
import com.example.MediaPlayer.Fragments.NormalVideoPlayerFragment;
import com.example.MediaPlayer.Fragments.PlaylistFragment;
import com.example.MediaPlayer.Fragments.SongPlayerFragment;
import com.example.MediaPlayer.Fragments.SongPlaylistFragment;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.Service.MyService;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;
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


    private FullscreenVideoPlayerFragment fullscreenPlayerFragment;
    private MainLayoutFragment mainAppLayoutFragment;
    private NormalVideoPlayerFragment normalPlayerFragment;
    private PlaylistFragment playlistFragment;

    SongPlayerFragment songPlayerFragment;
    private Intent backgroundIntent;

    private MediaPlayerViewModel mediaPlayerViewModel;

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

        setUpMainAppLayout();
//        hideBackButton();
        handler.post(makeFolderTree);

        setUpViewModel();
        getSavedData();
        backgroundIntent = new Intent(this, MyService.class);
        handler.post(initFragments);
    }

    private Runnable makeFolderTree = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "makeFolderTree: ");
            FolderRepository.getInstance().makeFolderTree(getApplicationContext());
            mediaPlayerViewModel.getIsFolderCreated().setValue(true);
        }
    };

    AlbumSongsFragment albumSongsFragment;
    Runnable initFragments = new Runnable() {
        @Override
        public void run() {
            normalPlayerFragment = new NormalVideoPlayerFragment();
            fullscreenPlayerFragment = new FullscreenVideoPlayerFragment();
            songPlayerFragment = new SongPlayerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment, songPlayerFragment)
                    .hide(songPlayerFragment)
                    .commit();
            albumSongsFragment = new AlbumSongsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment, albumSongsFragment)
                    .hide(albumSongsFragment)
                    .commit();
        }
    };


    public void setUpViewModel() {
        mediaPlayerViewModel = new ViewModelProvider(MainActivity.this).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getCurrentIndex().observe(this, position -> {
            backgroundIntent.putExtra(Utils.CURRENT_INDEX, position);
            if (VideoHistoryRepository.getInstance().getHistory() != null) {
                VideoHistoryRepository.getInstance().updateHistory(mediaPlayerViewModel.getCurrentMediaEntry());
            }
            saveVideoIndex(position);
        });

        mediaPlayerViewModel.getCurrentProcess().observe(this, integer ->
        {
            backgroundIntent.putExtra(Utils.CURRENT_PROGRESS, integer);
            saveVideoProcess(integer, mediaPlayerViewModel.getCurrentMediaEntry().getMediaName());
        });

        mediaPlayerViewModel.getCurrentPlaylist().observe(this, videoEntries -> {
            ParcelableVideoList videoListParcelable = new ParcelableVideoList(videoEntries);
            backgroundIntent.putExtra(Utils.CURRENT_PLAYLIST, videoListParcelable);
        });
    }

    public void getRepository() {
        if (checkPermission()) {
            // set up video repos
            VideoRepository.getInstance().getAllVideos(this);
            SongRepository.getInstance().getAudioList(this);
            GenreRepository.getInstance().getGenre(this);
            SongArtistRepository.getInstance().getAllArtists(this);
            AlbumRepository.getInstance().getAllAlbums(this);
            Log.d(TAG, "VideoRepository: " + VideoRepository.getInstance().getVideoList().size());
            Log.d(TAG, "AudioRepository: " + SongRepository.getInstance().getAudioList().size());
        }
    }

    public void getSavedData() {
        Log.d(TAG, "getSavedData: ");
        VideoHistoryRepository.getInstance().updateHistory(getSavedHistory());
        mediaPlayerViewModel.getCurrentPlaylist().setValue(getSavedPlaylist());
        Log.d(TAG, "getCurrentPlaylist: " + mediaPlayerViewModel.getCurrentPlaylist().getValue().size());
        mediaPlayerViewModel.getCurrentIndex().setValue(getSavedVideoPosition());
        mediaPlayerViewModel.getCurrentProcess().setValue(getSavedVideoProcess(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName()));
        mediaPlayerViewModel.getIsPauseSelected().setValue(true);
    }


    public void onVideoCompleted() {
        Log.d(TAG, "onVideoCompleted: ");
        String name = mediaPlayerViewModel.getCurrentMediaEntry().getMediaName();
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
        mediaPlayerViewModel.getCurrentPlaylist().setValue(VideoRepository.getInstance().getVideoList());
        mediaPlayerViewModel.getCurrentIndex().setValue(position);
    }

    public void getPLayList(ArrayList<Integer> videoPositions) {
        ArrayList<MediaEntry> videosInSelectedFolder = new ArrayList<>();
        for (int position : videoPositions) {
            videosInSelectedFolder.add(VideoRepository.getInstance().getVideoList().get(position));
        }
        mediaPlayerViewModel.getCurrentPlaylist().setValue(videosInSelectedFolder);
        mediaPlayerViewModel.getCurrentIndex().setValue(0);
    }

    public void playPrevVideo() {
        Log.d(TAG, "playPrevVideo: ");
        int currentPos = mediaPlayerViewModel.getCurrentIndex().getValue();
        if (Utils.isRepeatEnabled && currentPos == 0) {
            currentPos = mediaPlayerViewModel.getCurrentPlaylist().getValue().size();
        }

        if (currentPos > 0) {
            currentPos -= 1;
            mediaPlayerViewModel.getCurrentIndex().setValue(currentPos);
            mediaPlayerViewModel.getCurrentProcess().setValue(getSavedVideoProcess(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName()));
        }
    }

    public void playNextVideo() {
        Log.d(TAG, "playNextVideo: ");
        int currentPosition = mediaPlayerViewModel.getCurrentIndex().getValue();

        if (Utils.isRepeatEnabled && currentPosition == mediaPlayerViewModel.getCurrentPlaylist().getValue().size() - 1) {
            currentPosition = -1;
        }

        if (currentPosition < mediaPlayerViewModel.getCurrentPlaylist().getValue().size() - 1) {
            currentPosition += 1;
            mediaPlayerViewModel.getCurrentIndex().setValue(currentPosition);
            mediaPlayerViewModel.getCurrentProcess().setValue(getSavedVideoProcess(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName()));
        }
    }

    public void repeatOneVideo() {
        Log.d(TAG, "repeatOneVideo: ");
        mediaPlayerViewModel.getCurrentIndex().setValue(mediaPlayerViewModel.getCurrentIndex().getValue());
        mediaPlayerViewModel.getCurrentProcess().setValue(0);
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
                mediaPlayerViewModel.getCurrentIndex().setValue(shuffleIndices.get(currentShuffleIndex));
            }
        }

        mediaPlayerViewModel.getCurrentProcess().setValue(getSavedVideoProcess(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName()));
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

                mediaPlayerViewModel.getCurrentIndex().setValue(shuffleIndices.get(currentShuffleIndex));
            }
        }

        mediaPlayerViewModel.getCurrentProcess().setValue(getSavedVideoProcess(mediaPlayerViewModel.getCurrentMediaEntry().getMediaName()));
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

        shuffleIndices.add(mediaPlayerViewModel.getCurrentIndex().getValue());

        if (mediaPlayerViewModel.getCurrentPlaylist().getValue().size() > 2) {
            shuffleIndices.add(nextShuffleNumber());
            shuffleIndices.addAll(nextShuffleNumbers());
        }

        if (mediaPlayerViewModel.getCurrentPlaylist().getValue().size() == 2) {
            shuffleIndices.add(1 - mediaPlayerViewModel.getCurrentIndex().getValue());
        }
    }

    public ArrayList<Integer> nextShuffleNumbers() {
        ArrayList<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < mediaPlayerViewModel.getCurrentPlaylist().getValue().size(); i++) {
            if (!shuffleIndices.contains(i)) {
                randomList.add(i);
            }
        }
        Collections.shuffle(randomList);
        return randomList;
    }

    public int nextShuffleNumber() {
        int randomNumber = new Random().nextInt(mediaPlayerViewModel.getCurrentPlaylist().getValue().size());
        while (shuffleIndices.contains(randomNumber) || randomNumber == shuffleIndices.get(0) + 1) {
            randomNumber = new Random().nextInt(mediaPlayerViewModel.getCurrentPlaylist().getValue().size());
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


    public void setUpMainAppLayout() {
        mainAppLayoutFragment = new MainLayoutFragment();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment, mainAppLayoutFragment)
                .commit();

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


    public void showSongPlayerFragment() {
        Log.d(TAG, "enterMiniPlayer: ");
        getSupportFragmentManager()
                .beginTransaction()
                .show(songPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    private SongPlaylistFragment songPlaylistFragment;
    public  void addPlaylistFragment(){
        songPlaylistFragment = new SongPlaylistFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, songPlaylistFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showPlaylistFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .show(songPlaylistFragment)
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
            mediaPlayerViewModel.getCurrentPlaylist().setValue(parcelableVideoList.getVideoEntries());
            mediaPlayerViewModel.getCurrentIndex().setValue(intent.getIntExtra(Utils.CURRENT_INDEX, 0));
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
        backgroundIntent.putExtra(Utils.IS_PAUSE, mediaPlayerViewModel.getIsPauseSelected().getValue());
        backgroundIntent.putExtra(Utils.CURRENT_SHUFFLE_INDICES, shuffleIndices);
        backgroundIntent.putExtra(Utils.CURRENT_SHUFFLE_INDEX, currentShuffleIndex);
        if (!mediaPlayerViewModel.getIsPauseSelected().getValue()) {
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