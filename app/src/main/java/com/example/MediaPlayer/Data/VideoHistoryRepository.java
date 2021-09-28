package com.example.MediaPlayer.Data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VideoHistoryRepository {

    private static final String TAG = "VideoHistoryRepository";

    private List<MediaEntry> videoHistory = new ArrayList<>();
    private static VideoHistoryRepository instance;


    public static VideoHistoryRepository getInstance() {
        if (instance == null) {
            instance = new VideoHistoryRepository();
        }
        return instance;
    }

    public void updateHistory(List<MediaEntry> videoEntry) {
        videoHistory = videoEntry;
    }

    public void updateHistory(MediaEntry videoEntry) {
        Log.d(TAG, "updateHistory: ");

        if (isVideoExisted(videoEntry)) {
            videoHistory.remove(videoEntry);
        } else if (videoHistory.size() == 5) {
            videoHistory.remove(0);
        }

        videoHistory.add(videoEntry);
    }

    public boolean isVideoExisted(MediaEntry video) {
        Log.d(TAG, "videoHistory: " + videoHistory.size());
        if (videoHistory.size() > 0) {
            for (MediaEntry videoEntry : videoHistory) {
                if (video.getMediaName().equals(videoEntry.getMediaName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<MediaEntry> getHistory() {
        return videoHistory;
    }


}


