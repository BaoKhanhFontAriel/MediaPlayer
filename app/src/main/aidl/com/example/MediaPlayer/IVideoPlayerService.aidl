// IVideoPlayerService.aidl
package com.example.MediaPlayer;

// Declare any non-default types here with import statements
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.ICallback;

interface IVideoPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<MediaEntry> getVideos(in String query);
    void playSelectedVideo(in MediaEntry videoEntry);
    void playNext();
    void playPrev();
    void pause();
    void play();
    void registerCb(ICallback cb);
    void unRegisterCb(ICallback cb);
}