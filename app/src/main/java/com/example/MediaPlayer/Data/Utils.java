package com.example.MediaPlayer.Data;

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
}
