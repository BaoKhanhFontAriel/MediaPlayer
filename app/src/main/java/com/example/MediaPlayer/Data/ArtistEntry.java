package com.example.MediaPlayer.Data;

import android.net.Uri;

public class ArtistEntry {
    private String artist;
    private Uri uri;


    public ArtistEntry(String artist, Uri uri) {
        this.artist = artist;
        this.uri = uri;
    }


    public String getArtist() {
        return artist;
    }

    public Uri getUri() {
        return uri;
    }
}
