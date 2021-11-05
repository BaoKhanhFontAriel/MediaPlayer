package com.example.MediaPlayer.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Size;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Utils {
    public enum PLAY_MODE {
        SHUFFLE,
        AUTO_NEXT,
        REPEAT_ONE,
    }

    public static PLAY_MODE playMode = PLAY_MODE.AUTO_NEXT;

    public static boolean isRepeatEnabled;

    public static int DELAY_MILLIS = 3000;

    public static boolean isShuffleButtonSelected;

    public static String REQUEST_KEY = "requestKey";

    public static final String VIDEO_TRANSITION = "VIDEO_TRANSITION";
    public static final String BUTTON_PANEL_TRANSITION = "BUTTON_PANEL_TRANSITION";
    public static final String PLAYLIST_TRANSITION = "PLAYLIST_TRANSITION";
    public static final String DETAIL_TRANSITION = "DETAIL_TRANSITION";
    public static final String PROGRESS_BAR_TRANSITION = "PROGRESS_BAR_TRANSITION";

    public static final String CURRENT_INDEX = "CURRENT_INDEX";
    public static final String CURRENT_VIDEO_NAME = "CURRENT_VIDEO_NAME";
    public static final String CURRENT_PLAYLIST = "CURRENT_PLAYLIST";
    public static final String CURRENT_PROGRESS = "CURRENT_PROGRESS";
    public static final String CURRENT_SHUFFLE_INDICES = "CURRENT_SHUFFLE_INDICES";
    public static final String CURRENT_SHUFFLE_INDEX = "CURRENT_SHUFFLE_INDEX";
    public static final String IS_PAUSE = "IS_PAUSE";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Bitmap getThumbnail(Uri uri, Context context){
        Bitmap thumb = null;
        try {
            thumb = context.getContentResolver().loadThumbnail(uri, new Size(200, 200), new CancellationSignal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }

    public  static String convertTime(int duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Bitmap getThumbnail(Context context, Uri uri, int width, int height){
        Bitmap thumb = null;
        try {
            thumb = context.getContentResolver().loadThumbnail(uri, new Size(width, height), new CancellationSignal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }

}
