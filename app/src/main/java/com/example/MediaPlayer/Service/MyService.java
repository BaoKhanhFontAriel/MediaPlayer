package com.example.MediaPlayer.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.Data.ParcelableVideoList;
import com.example.MediaPlayer.ICallback;
import com.example.MediaPlayer.IVideoPlayerService;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.R;

import java.io.IOException;
import java.util.List;

public class MyService extends Service {
    private final static String TAG = "MyService";

    final RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<ICallback>();

    private MediaPlayer mediaPlayer;
    private int currentIndex;
    private List<MediaEntry> currPlaylist;
    private MediaEntry currVideo;
    private int currentProgress;
    private Intent intentToMain;
    private boolean isPause = false;
    private final String NOTIFICATION_CHANNEL = "video_player_channel";
    private final int NOTIFICATION_ID = 1337;
    ICallback iCallback;

    Handler handler = new Handler(Looper.getMainLooper());

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();
        mediaPlayer = new MediaPlayer();
        intentToMain = new Intent("com.example.videoplayer.MyService");
    }

    private final IVideoPlayerService.Stub binder = new IVideoPlayerService.Stub() {

        @Override
        public List<MediaEntry> getVideos(String query) throws RemoteException {
            return VideoRepository.getInstance().getFilteredVideos(getApplicationContext(), query);
        }

        @Override
        public void playSelectedVideo(MediaEntry videoEntry) throws RemoteException {
            Log.d(TAG, "playSelectedVideo: ");
            currentIndex = onFindVideoIndex(videoEntry);
            Log.d(TAG, "currentIndex: " + currentIndex);
            Log.d(TAG, "currPlaylist: " + currPlaylist.size());

            try {
                playVideo(currentIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int findVideoIndex(MediaEntry videoEntry){
            for (int i = 0; i < currPlaylist.size(); i++){
                if (currPlaylist.get(i).getMediaName().equals(videoEntry.getMediaName())){
                    return i;
                }
            }
            return -1;
        }

        public int onFindVideoIndex(MediaEntry videoEntry){
            int index = findVideoIndex(videoEntry);
            if (index != -1){
                return index;
            }
            else {
                currPlaylist.add(videoEntry);
                return currPlaylist.size() - 1;
            }
        }

        @Override
        public void playNext() throws RemoteException {
            try {
                onPlayNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void playPrev() throws RemoteException {
            try {
                onPlayPrev();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pause() throws RemoteException {
            onPause();
        }

        @Override
        public void play() throws RemoteException {
            onPlay();
        }

        @Override
        public void registerCb(ICallback cb) throws RemoteException {
            if (cb != null) {
                Log.d(TAG, "registerCb: ");
                mCallbacks.register(cb);
                setClientCallback();
//                handler.post(updateProgress);
            }
        }

        @Override
        public void unRegisterCb(ICallback cb) throws RemoteException {
            if (cb != null) {
                mCallbacks.unregister(cb);
            }
        }
    };

    public void startForeground() {
        Log.d(TAG, "startForeground: ");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d(TAG, "Notification.Builder: ");
            createNotificationChannel();
            notification = new Notification.Builder(this, NOTIFICATION_CHANNEL)
                    .setOngoing(true)
                    .setChannelId(NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("video player")
                    .setContentText("play video")
                    .setContentIntent(pendingIntent)
                    .build();
        }
        // Notification ID cannot be 0.
        startForeground(NOTIFICATION_ID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ParcelableVideoList videoListParcelable = intent.getParcelableExtra(Utils.CURRENT_PLAYLIST);
        currPlaylist = videoListParcelable.getVideoEntries();
        currentIndex = intent.getIntExtra(Utils.CURRENT_INDEX, 0);
        currentProgress = intent.getIntExtra(Utils.CURRENT_PROGRESS, 0);
        isPause = intent.getBooleanExtra(Utils.IS_PAUSE, false);
        Log.d(TAG, "isPause: " + isPause);

            try {
                playVideo(currentIndex);
            } catch (IOException | RemoteException e) {
                e.printStackTrace();
            }


        return START_STICKY;
    }

    public void playVideo(int index) throws IOException, RemoteException {
        Log.d(TAG, "playVideo: ");

        // clear all prev video process
        mediaPlayer.reset();
        handler.removeCallbacks(updateProgress);

        // start new video process
        currVideo = currPlaylist.get(index);
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(currVideo.getUri()));
        mediaPlayer.prepare();
        mediaPlayer.seekTo(currentProgress);
        mediaPlayer.start();
        intentToMain.putExtra(Utils.CURRENT_INDEX, currentIndex);
        setClientCallback();

        mediaPlayer.setOnCompletionListener(mp -> {
            try {
                mp.reset();
                onPlayNext();
            } catch (IOException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    public void onPlayNext() throws IOException, RemoteException {
        if (currentIndex < currPlaylist.size() - 1) {
            currentIndex++;
            playVideo(currentIndex);
        }
    }

    public void onPlayPrev() throws IOException, RemoteException {
        if (currentIndex > 0) {
            currentIndex--;
            playVideo(currentIndex);
        }
    }

    public void onPlay() {
        mediaPlayer.start();
    }

    public void onPause() {
        mediaPlayer.pause();
    }

    public void setClientCallback() throws RemoteException {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                iCallback = mCallbacks.getBroadcastItem(i);
                iCallback.getVideoName(currVideo.getMediaName());
                iCallback.getArtistName(currVideo.getArtistName());
                iCallback.getUri(currVideo.getUri().toString());
                iCallback.getDuration(currVideo.getDuration());
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
        handler.post(updateProgress);
    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            final int N = mCallbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    iCallback = mCallbacks.getBroadcastItem(i);
                    iCallback.getProgress(mediaPlayer.getCurrentPosition());
                    currentProgress = mediaPlayer.getCurrentPosition();
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            mCallbacks.finishBroadcast();
            handler.postDelayed(updateProgress, 1000);

        }
    };

    public void putIntentToMain() {
        intentToMain.putExtra(Utils.CURRENT_VIDEO_NAME, currVideo.getMediaName());
        intentToMain.putExtra(Utils.CURRENT_INDEX, currentIndex);
        intentToMain.putExtra(Utils.CURRENT_PLAYLIST, new ParcelableVideoList(currPlaylist));
        Log.d(TAG, "currentProgress: " + currentProgress);
        intentToMain.putExtra(Utils.CURRENT_PROGRESS, currentProgress);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mediaPlayer.stop();
        putIntentToMain();
        sendBroadcast(intentToMain);
        mCallbacks.kill();
    }
}