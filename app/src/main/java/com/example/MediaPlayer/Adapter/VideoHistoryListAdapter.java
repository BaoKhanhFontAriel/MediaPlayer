package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.view.View;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;

import java.util.ArrayList;

public class VideoHistoryListAdapter extends VideoListAdapter{

    public VideoHistoryListAdapter(ArrayList<MediaEntry> videoEntries, BaseListAdapter.IEntryClicked callback, Context context) {
        super(videoEntries, callback, context);
        setLayoutId(R.layout.video_folder_grid_ui);
        setFolderLayoutId(R.id.folder_layout);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.folder_layout.setVisibility(View.GONE);
    }
}
