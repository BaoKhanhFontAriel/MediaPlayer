package com.example.MediaPlayer.Data;

import java.util.List;

public class GenreEntry {
    private String genre;
    private List<String> songs_within;


    public GenreEntry(String genre, List<String> songs_within) {
        this.genre = genre;
        this.songs_within = songs_within;
    }

    public String getGenre() {
        return genre;
    }

    public List<String> getSongs_within() {
        return songs_within;
    }
}
