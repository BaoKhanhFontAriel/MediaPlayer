package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;

import java.util.List;

public class SongArtistAlbumListAdapter extends BaseListAdapter{
    private List<AlbumEntry> albums;

    public SongArtistAlbumListAdapter(List<AlbumEntry> songs, IEntryClicked callback, Context context) {
        super(callback, context);
        this.albums = songs;
        setLayoutId(R.layout.album_item_artist_frag);
        setTitleId(R.id.album_name_artist_frag);
        setThumbnailId(R.id.album_thumb_artist_frag);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        AlbumEntry album = albums.get(position);
        holder.title.setText(album.getAlbum());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.thumbnail.setImageBitmap(Utils.getThumbnail(album.getUri(), getContext()));
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
