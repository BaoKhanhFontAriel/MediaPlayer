package com.example.MediaPlayer.Data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GenreRepository {
    private static final String TAG = "GenreRepository";


    private List<GenreEntry> genreList = new ArrayList<>();

    private static GenreRepository instance;

    public static GenreRepository getInstance() {
        if (instance == null) {
            instance = new GenreRepository();
        }
        return instance;
    }

    public List<GenreEntry> getGenreList(){
        return genreList;
    }

    public void getGenre(Context context) {
        String[] genreProjection = new String[]{
                MediaStore.Audio.Genres._ID, // 0
                MediaStore.Audio.Genres.NAME, // 1
        };

        try (Cursor genreCursor = context.getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                genreProjection,
                null,
                null,
                MediaStore.Audio.Genres.NAME + " ASC"
        )) {
            int nameColumn = genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
            int idColumn = genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);

            while (genreCursor.moveToNext()) {
                long id = genreCursor.getLong(idColumn);
                String genre = genreCursor.getString(nameColumn);
                List<String> songsWithin = getSongWithin(context,id);
                genreList.add(new GenreEntry(genre, songsWithin));
            }
        }
    }

    public List<String> getSongWithin(Context context, long id) {
        List<String> songs = new ArrayList<>();

        String[] songProjection = new String[]{
                MediaStore.Audio.Genres.Members._ID, // 0
                MediaStore.Audio.Genres.Members.DISPLAY_NAME // 1
        };

        try (Cursor songCursor = context.getContentResolver().query(
                MediaStore.Audio.Genres.Members.getContentUri("external", id),
                songProjection,
                null,
                null,
                null
        )) {
            int isColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members._ID);
            int songColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.DISPLAY_NAME);

            while (songCursor.moveToNext()) {
                String song = songCursor.getString(songColumn);
                songs.add(song);
            }
        }
        Log.d(TAG, "songs.size: " + songs.size());
        return songs;
    }
}
