package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.net.Uri;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;

import java.util.ArrayList;

public class TrackListAdapter extends BaseListAdapter {

    private ArrayList<MediaEntry> audioList;

    public TrackListAdapter(ArrayList<MediaEntry> audioEntries, IEntryClicked callback, Context context) {
        super(callback, context);
        this.audioList = audioEntries;
        setLayoutId(R.layout.video_entry_ui);
        setTitleId(R.id.video_name);
        setDetailId(R.id.artist_and_duration);
        setThumbnailId(R.id.thumbnail);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        MediaEntry audio = audioList.get(position);
        holder.title.setText(audio.getMediaName());
        String artistAndDuration = audio.getArtistName() + " - " + convertTime(audio.getDuration());
        holder.detail.setText(artistAndDuration);
        holder.thumbnail.setImageBitmap(getThumbnail(Uri.parse(audio.getUri())));
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }
}
