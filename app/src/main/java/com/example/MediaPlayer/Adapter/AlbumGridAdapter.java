package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.R;

import java.util.List;

public class AlbumGridAdapter extends BaseGridAdapter{
    List<AlbumEntry> albumEntries;
    private TextView album_name;
    private TextView artist_name;
    private ImageView album_thumb;

    public AlbumGridAdapter(Context mContext, List<AlbumEntry> albumEntries) {
        super(mContext);
        this.albumEntries = albumEntries;
    }

    @Override
    public int getLayoutId() {
        return R.layout.alnum_ui;
    }

    @Override
    public int getCount() {
        return albumEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return albumEntries.get(position);
    }

    @Override
    public void setId(View view) {
        super.setId(view);
        album_name = view.findViewById(R.id.album_name);
        artist_name = view.findViewById(R.id.album_artist);
        album_thumb = view.findViewById(R.id.album_thumb);
    }

    @Override
    public void assignView(int position) {
        super.assignView(position);
        album_name.setText(albumEntries.get(position).getAlbum());
        artist_name.setText(albumEntries.get(position).getArtist());
        album_thumb.setImageBitmap(getThumbnail(albumEntries.get(position).getUri()));
    }
}
