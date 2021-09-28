package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.net.Uri;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;

import java.util.ArrayList;

public class VideoListAdapter extends BaseListAdapter {

    private ArrayList<MediaEntry> videoEntryArrayList;

    public VideoListAdapter(ArrayList<MediaEntry> videoEntries, BaseListAdapter.IEntryClicked callback, Context context) {
        super(callback, context);
        this.videoEntryArrayList = videoEntries;
        setLayoutId(R.layout.video_entry_ui);
        setTitleId(R.id.video_name);
        setDetailId(R.id.artist_and_duration);
        setThumbnailId(R.id.thumbnail);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        MediaEntry videoEntry = videoEntryArrayList.get(position);

        holder.title.setText(videoEntry.getMediaName());
        String artistAndDuration = videoEntry.getArtistName() + " - " + convertTime(videoEntry.getDuration());
        holder.detail.setText(artistAndDuration);
        holder.thumbnail.setImageBitmap(getThumbnail(Uri.parse(videoEntry.getUri())));
    }

    @Override
    public int getItemCount() {
        return videoEntryArrayList.size();
    }
}
