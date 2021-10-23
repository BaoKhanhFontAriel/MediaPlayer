package com.example.MediaPlayer.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AudioRepository {
    private static final String TAG = "AudioRepository";

    private ArrayList<MediaEntry> audioList = new ArrayList<>();
    private ArrayList<MediaEntry> filteredAudioList = new ArrayList<>();

    private static AudioRepository instance;

    public static AudioRepository getInstance() {
        if (instance == null) {
            instance = new AudioRepository();
        }
        return instance;
    }

    public ArrayList<MediaEntry> getAudioList() {
        return audioList;
    }

    public void getAudioList(Context context) {
        Log.d(TAG, "getAllAudios: ");

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.GENRE,
                MediaStore.Audio.Media.VOLUME_NAME,
        };

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int genreColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE);
            int volumeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.VOLUME_NAME);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String displayName = cursor.getString(displayNameColumn);
                String path = cursor.getString(pathColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                String genre = cursor.getString(genreColumn);
                String volume = cursor.getString(volumeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        collection, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                audioList.add(new MediaEntry(id, contentUri.toString(), displayName, path, name, artist, album, duration, volume));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public List<MediaEntry> getFilteredAudio(Context context, String location, String locationName) {
        List<MediaEntry> songs = new ArrayList<>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.VOLUME_NAME,
        };

        String[] genresProjection = {
                MediaStore.Audio.Genres.NAME,
                MediaStore.Audio.Genres._ID
        };


        String where = location + " = ?";
        String[] whereVals = {locationName};

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                where,
                whereVals,
                MediaStore.Audio.Media.TITLE + " ASC"
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int volumeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.VOLUME_NAME);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String displayName = cursor.getString(displayNameColumn);
                String path = cursor.getString(pathColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                String volume = cursor.getString(volumeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        collection, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                songs.add(new MediaEntry(id, contentUri.toString(), displayName, path, name, artist, album, duration, volume));
            }
        }
        return songs;
    }
}
