package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MediaPlayer.R;

import java.util.List;

public class ArtistGridAdapter extends BaseGridAdapter {

    private List<String> mItems;
    private TextView name;
    private ImageView artistIcon;

    public ArtistGridAdapter(List<String> items, Context context) {
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
        return R.layout.artist_ui;
    }

    @Override
    public void setId(View view) {
        name = view.findViewById(R.id.artist_name);
        artistIcon = view.findViewById(R.id.artist_icon);
    }

    @Override
    public void assignView(int position) {
        super.assignView(position);
        name.setText(mItems.get(position));
    }
}
