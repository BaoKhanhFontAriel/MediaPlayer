package com.example.MediaPlayer.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlbumRepository {
    private static final String TAG = "AlbumRepository";

    private List<AlbumEntry> albumList = new ArrayList<>();

    private static AlbumRepository instance;

    public static AlbumRepository getInstance() {
        if (instance == null) {
            instance = new AlbumRepository();
        }
        return instance;
    }

    public List<AlbumEntry> getAlbumList(){
        return albumList;
    }

    public void getAllAlbums(Context context) {
        String[] genreProjection = new String[]{
                MediaStore.Audio.Albums._ID, // 0
                MediaStore.Audio.Albums.ALBUM, // 1
                MediaStore.Audio.Albums.ARTIST, // 1
        };

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                genreProjection,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM + " ASC"
        )) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String album = cursor.getString(nameColumn);
                String artist = cursor.getString(artistColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id);

                albumList.add(new AlbumEntry(album, artist, contentUri));
            }
        }
    }
}
