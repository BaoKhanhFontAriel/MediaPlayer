// ICallback.aidl
package com.example.MediaPlayer;

// Declare any non-default types here with import statements
import com.example.MediaPlayer.Data.MediaEntry;

interface ICallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
        void getVideoName(String videoname);
        void getArtistName(String artist);
        void getUri(String uri);
        void getDuration(int duration);
        void getProgress(int progress);
}