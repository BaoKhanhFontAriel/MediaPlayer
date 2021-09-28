package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.MediaPlayer.Data.ArtistEntry;
import com.example.MediaPlayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MusicArtistListAdapter extends BaseListAdapter {
    private static final String TAG = "MusicArtistListAdapter";
    private List<ArtistEntry> artistList = new ArrayList<>();

    public MusicArtistListAdapter(List<ArtistEntry> artistList, IEntryClicked callback, Context context) {
        super(callback, context);
        this.artistList = artistList;
        setLayoutId(R.layout.music_artist_ui);
        setTitleId(R.id.music_artist_title);
        setThumbnailId(R.id.music_thumbnail);
        Log.d(TAG, "MusicArtistListAdapter: ");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        super.onBindViewHolder(holder, position);
            holder.title.setText(artistList.get(position).getArtist());
            holder.thumbnail.setImageBitmap(getThumbnail(artistList.get(position).getUri()));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }
}

