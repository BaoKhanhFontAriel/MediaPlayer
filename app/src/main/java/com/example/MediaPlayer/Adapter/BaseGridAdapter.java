package com.example.MediaPlayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseGridAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private final Context mContext;

    public BaseGridAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(getLayoutId(), null);
        }
        setId(convertView);
        assignView(position);
        return convertView;
    }

    public int getLayoutId(){
        return 0;
    }

    public void assignView(int position){

    }

    public void setId(View view){

    }

    @SuppressLint("DefaultLocale")
    public java.lang.String convertToMinutes(int duration) {
        return java.lang.String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));

    }

    public Bitmap getThumbnail(Uri uri) {
        Size mSize = new Size(200, 120);
        CancellationSignal ca = new CancellationSignal();
        Bitmap thumb = null;
        try {
            thumb = mContext.getContentResolver().loadThumbnail(uri, mSize, ca);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }
}
