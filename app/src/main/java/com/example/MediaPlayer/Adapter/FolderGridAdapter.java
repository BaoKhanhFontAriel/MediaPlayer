package com.example.MediaPlayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.Data.TreeNode;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FolderGridAdapter extends BaseAdapter {
    private static final String TAG = "FolderGridAdapter";

    private final LayoutInflater layoutInflater;
    private List<TreeNode<String>> items;

    private TextView videoName;
    private TextView artistAndDuration;
    private ImageView thumbnail;
    private ImageView folder_icon;
    private ImageView preview_thumbnail;
    private TextView folder_title;
    private RelativeLayout folder_layout;
    private LinearLayout video_layout;
    private final Context context;
    private final int switchPosition;

    public FolderGridAdapter(Context context, List<TreeNode<String>> items, int switchPosition) {
        this.context = context;
        this.items = items;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.switchPosition = switchPosition;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.video_folder_grid_ui, null);
        }

        setId(convertView);

        if (position < switchPosition) {
            Log.d(TAG, "in video : ");
            video_layout.setVisibility(View.VISIBLE);
            folder_layout.setVisibility(View.GONE);

            setUpVideoFile(position);
        } else {
            Log.d(TAG, "in folder: ");
            video_layout.setVisibility(View.GONE);
            folder_layout.setVisibility(View.VISIBLE);

            setUpFolder(position);
        }

        return convertView;
    }

    public void setId(View convertView) {
        video_layout = convertView.findViewById(R.id.video_layout);
        videoName = convertView.findViewById(R.id.video_name);
        artistAndDuration = convertView.findViewById(R.id.artist_and_duration);
        thumbnail = convertView.findViewById(R.id.thumbnail);
        folder_layout = convertView.findViewById(R.id.folder_layout);
        folder_title = convertView.findViewById(R.id.folder_text);
        folder_icon = convertView.findViewById(R.id.folder);
        preview_thumbnail = convertView.findViewById(R.id.preview_thumbnail);
    }

    public void setUpFolder(int position) {
        TreeNode<String> folder = items.get(position);
        String name = folder.getPath().substring(
                items.get(position).getPath().lastIndexOf("/") + 1);
        folder_title.setText(name);
        while (folder.getVideoChildren().size() == 0) {
            folder = folder.getFolderChildren().get(0);
        }
        MediaEntry videoEntry = getVideo(folder.getVideoChildren().get(0).getVideoPosition());
        preview_thumbnail.setImageBitmap(getThumbnail(Uri.parse(videoEntry.getUri())));
    }

    public void setUpVideoFile(int position) {

        int index = items.get(position).getVideoPosition();

        MediaEntry videoEntry = VideoRepository.getInstance().getVideoList().get(index);

        String name = videoEntry.getMediaName();
        int duration = videoEntry.getDuration();
        String album = videoEntry.getAlbum();

        java.lang.String videoTitle = album + " - " + convertToMinutes(duration);

        artistAndDuration.setText(videoTitle);
        videoName.setText(name);
        thumbnail.setImageBitmap(getThumbnail(Uri.parse(videoEntry.getUri())));
    }

    private MediaEntry getVideo(int index){
        return VideoRepository.getInstance().getVideoList().get(index);
    }

    @SuppressLint("DefaultLocale")
    public java.lang.String convertToMinutes(int duration) {
        return java.lang.String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));

    }

    public Bitmap getThumbnail(Uri uri) {
        Size mSize = new Size(200, 120);
        CancellationSignal ca = new CancellationSignal();
        Bitmap thumb = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                thumb = context.getContentResolver().loadThumbnail(uri, mSize, ca);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }
}
