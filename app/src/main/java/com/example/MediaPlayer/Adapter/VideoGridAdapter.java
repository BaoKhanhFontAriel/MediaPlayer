package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;

import java.util.List;

public class VideoGridAdapter extends BaseGridAdapter {

    private List<MediaEntry> mItems;

    private TextView videoName;
    private TextView album;
    private ImageView thumbnail;
    private RelativeLayout folder_layout;


    public VideoGridAdapter(Context context, List<MediaEntry> items) {
        super(context);
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public int getLayoutId() {
        return R.layout.video_folder_grid_ui;
    }

    @Override
    public void setId(View itemView) {
        videoName = itemView.findViewById(R.id.media_name_mini_song_player);
        album = itemView.findViewById(R.id.artist_and_duration);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        folder_layout = itemView.findViewById(R.id.folder_layout);
    }

    @Override
    public void assignView(int position) {
        super.assignView(position);
        MediaEntry videoEntry = mItems.get(position);
        videoName.setText(videoEntry.getMediaName());
        album.setText(videoEntry.getAlbum());
        thumbnail.setImageBitmap(getThumbnail(Uri.parse(videoEntry.getUri())));
        folder_layout.setVisibility(View.GONE);
    }
}
