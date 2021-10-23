package com.example.MediaPlayer.Data;

import android.net.Uri;

import java.util.List;

public class AlbumEntry {
    private String album;
    private String artist;
    private Uri uri;

    public AlbumEntry(String genre, String artist, Uri uri) {
        this.album = genre;
        this.artist = artist;
        this.uri = uri;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public Uri getUri() {
        return uri;
    }
}
