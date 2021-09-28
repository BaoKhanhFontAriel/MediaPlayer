package com.example.MediaPlayer.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlbumListAdapter extends BaseListAdapter{
    private List<AlbumEntry> albumEntries;

    public AlbumListAdapter(IEntryClicked callback, Context context, List<AlbumEntry> albumEntries) {
        super(callback, context);
        this.albumEntries = albumEntries;
        setLayoutId(R.layout.alnum_ui);
        setTitleId(R.id.album_name);
        setDetailId(R.id.album_artist);
        setThumbnailId(R.id.album_thumb);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.title.setText(albumEntries.get(position).getAlbum());
        holder.detail.setText(albumEntries.get(position).getArtist());
//        Bitmap image = getThumbnail(albumEntries.get(position).getUri());
//        if (image != null) {
//            holder.thumbnail.setImageBitmap(image);
//        }
    }

    @Override
    public int getItemCount() {
        return albumEntries.size();
    }
}
