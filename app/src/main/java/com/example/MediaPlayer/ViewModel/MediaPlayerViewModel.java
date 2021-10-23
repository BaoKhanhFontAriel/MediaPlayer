package com.example.MediaPlayer.ViewModel;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.Data.MediaEntry;

import java.util.List;

public class MediaPlayerViewModel extends ViewModel {
    private static final String TAG = "PlaylistViewModel";


    private MutableLiveData<List<MediaEntry>> currentPlaylist = new MutableLiveData<>();
    private MutableLiveData<Integer> currentIndex = new MutableLiveData<>();
    private MutableLiveData<Integer> currentProcess = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPauseSelected = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFolderCreated = new MutableLiveData<>();
    private MutableLiveData<Boolean> isShuffleSelected = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVideoClicked = new MutableLiveData<>();
    private MutableLiveData<String> artistName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSong = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDragging = new MutableLiveData<>();
    private MutableLiveData<MediaPlayer> mediaPlayerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AlbumEntry> albumEntryMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MediaEntry>> genreSongEntryMutableLiveData = new MutableLiveData<>();



    public  MutableLiveData<List<MediaEntry>> getCurrentPlaylist(){
        return currentPlaylist;
    }

    public MutableLiveData<Integer> getCurrentIndex(){
        return currentIndex;
    }

    public MutableLiveData<Integer> getCurrentProcess(){
        return currentProcess;
    }

    public MediaEntry getCurrentMediaEntry(){
            return currentPlaylist.getValue().get(currentIndex.getValue());
    }

    public MutableLiveData<Boolean> getIsPauseSelected() {
        Log.d(TAG, "getIsPauseSelected: ");
        return isPauseSelected;
    }

    public MutableLiveData<Boolean> getIsFolderCreated() {
        return isFolderCreated;
    }

    public MutableLiveData<Boolean> getIsShuffleSelected() {
        return isShuffleSelected;
    }

    public MutableLiveData<Boolean> getIsVideoClicked() {
        return isVideoClicked;
    }

    public MutableLiveData<String> getArtistName() {
        return artistName;
    }

    public MutableLiveData<Boolean> getIsSong() {
        return isSong;
    }

    public MutableLiveData<Boolean> getIsDragging() {
        return isDragging;
    }

    public MutableLiveData<MediaPlayer> getMediaPlayerMutableLiveData() {
        return mediaPlayerMutableLiveData;
    }

    public MutableLiveData<AlbumEntry> getAlbumEntryMutableLiveData() {
        return albumEntryMutableLiveData;
    }

    public MutableLiveData<List<MediaEntry>> getGenreSongEntryMutableLiveData() {
        return genreSongEntryMutableLiveData;
    }
}
