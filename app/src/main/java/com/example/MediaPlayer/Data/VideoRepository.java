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

public class VideoRepository {
    private final static String TAG = "VideoRepository";

    private List<MediaEntry> videoList = new ArrayList<>();
    private static VideoRepository instance;

    public static VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    public List<MediaEntry> getVideoList() {
        return videoList;
    }

    public void getAllVideos(Context context) {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.RELATIVE_PATH,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.ALBUM,
                MediaStore.Video.Media.VOLUME_NAME
        };

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                null,
                null,
                MediaStore.Video.Media.TITLE + " ASC"
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM);
            int volumeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.VOLUME_NAME);

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
                videoList.add(new MediaEntry(id, contentUri.toString(), displayName, path, name, artist, album, duration, volume));
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public List<MediaEntry> getFilteredVideos(Context context, String query) {
        Log.d(TAG, "query: " + query);
        List<MediaEntry> videoEntries = new ArrayList<>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.RELATIVE_PATH,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.ALBUM,
                MediaStore.Video.Media.VOLUME_NAME
        };

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                MediaStore.Video.Media.TITLE + " LIKE ?",
                new String[]{"%"+query+"%"},
                MediaStore.Video.Media.TITLE + " ASC"
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM);
            int volumeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.VOLUME_NAME);

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
                videoEntries.add(new MediaEntry(id, contentUri.toString(), displayName, path, name, artist, album, duration, volume));
            }
        }

        return videoEntries;
    }

}
