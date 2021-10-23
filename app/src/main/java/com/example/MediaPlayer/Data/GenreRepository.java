package com.example.MediaPlayer.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
                List<MediaEntry> songsWithin = getSongWithin(context,id);
                genreList.add(new GenreEntry(genre, songsWithin));
            }
        }
    }

    public List<MediaEntry> getSongWithin(Context context, long id) {
        List<MediaEntry> songs = new ArrayList<>();


        Uri collection = MediaStore.Audio.Genres.Members.getContentUri("external", id);

        String[] songProjection = new String[]{
                MediaStore.Audio.Genres.Members._ID,
                MediaStore.Audio.Genres.Members.DISPLAY_NAME,
                MediaStore.Audio.Genres.Members.RELATIVE_PATH,
                MediaStore.Audio.Genres.Members.TITLE,
                MediaStore.Audio.Genres.Members.DURATION,
                MediaStore.Audio.Genres.Members.ARTIST,
                MediaStore.Audio.Genres.Members.ALBUM,
                MediaStore.Audio.Genres.Members.VOLUME_NAME,
        };

        try (Cursor songCursor = context.getContentResolver().query(
                collection,
                songProjection,
                null,
                null,
                null
        )) {
            int idColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members._ID);
            int displayNameColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.DISPLAY_NAME);
            int pathColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.RELATIVE_PATH);
            int nameColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.TITLE);
            int durationColumn =
                    songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.DURATION);
            int artistColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.ARTIST);
            int albumColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.ALBUM);
            int volumeColumn = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.VOLUME_NAME);

            while (songCursor.moveToNext()) {
                long songId = songCursor.getLong(idColumn);
                String displayName = songCursor.getString(displayNameColumn);
                String path = songCursor.getString(pathColumn);
                String name = songCursor.getString(nameColumn);
                int duration = songCursor.getInt(durationColumn);
                String artist = songCursor.getString(artistColumn);
                String album = songCursor.getString(albumColumn);
                String volume = songCursor.getString(volumeColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        collection, id);
                songs.add(new MediaEntry(songId, contentUri.toString(), displayName, path, name, artist, album, duration, volume));
            }
        }
        Log.d(TAG, "songs.size: " + songs.size());
        return songs;
    }
}
