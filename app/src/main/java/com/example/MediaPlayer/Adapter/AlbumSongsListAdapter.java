package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;

import java.util.List;

public class AlbumSongsListAdapter extends BaseListAdapter{
    private static final String TAG = "AlbumSongsListAdapter";

    private List<MediaEntry> songs;

    public AlbumSongsListAdapter(List<MediaEntry> songs, IEntryClicked callback, Context context) {
        super(callback, context);
        Log.d(TAG, "AlbumSongsListAdapter: ");
        this.songs = songs;
        setLayoutId(R.layout.alnum_song_ui);
        setTitleId(R.id.song_name_recyclerview_album);
        setDetailId(R.id.duration_recyclerview_album);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        MediaEntry song = songs.get(position);
        Log.d(TAG, "song: " + song.getMediaName());
        holder.title.setText(song.getMediaName());
        String duration = Utils.convertTime(song.getDuration());
        holder.detail.setText(duration);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
