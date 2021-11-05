package com.example.MediaPlayer.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class SongArtistRepository {
    private List<ArtistEntry> artistList = new ArrayList<>();

    private static SongArtistRepository instance;

    public static SongArtistRepository getInstance() {
        if (instance == null) {
            instance = new SongArtistRepository();
        }
        return instance;
    }

    public List<ArtistEntry> getArtistList(){
        return artistList;
    }

    public void getAllArtists(Context context) {
        String[] genreProjection = new String[]{
                MediaStore.Audio.Artists._ID, // 0
                MediaStore.Audio.Artists.ARTIST // 1
        };

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                genreProjection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC"
        )) {
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
            int idColumn = cursor.getColumnIndexOrThrow( MediaStore.Audio.Artists._ID);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String artist = cursor.getString(nameColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, id);

                artistList.add(new ArtistEntry(artist, contentUri));
            }
        }
    }
}
