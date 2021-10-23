package com.example.MediaPlayer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.BaseViewHolder> {
    private static final String TAG = "BaseAudioListAdapter";

    private Context context;
    private View currView;
    private int layoutId;
    private int titleId;
    private int detailId;
    private int thumbnailId;
    private int folderLayoutId;

    public BaseListAdapter(IEntryClicked callback, Context context){
        Log.d(TAG, "BaseAudioListAdapter: ");
        this.callback = callback;
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    @NonNull
    @NotNull
    @Override
    public BaseListAdapter.BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BaseViewHolder(inflater.inflate(getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseListAdapter.BaseViewHolder holder, int position) {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Bitmap getThumbnail(Uri uri){
        Bitmap thumb = null;
        try {
            thumb = context.getContentResolver().loadThumbnail(uri, new Size(200, 120), new CancellationSignal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumb;
    }

    public String convertTime(int duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) duration),
                TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                duration)));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView detail;
        ImageView thumbnail;
        RelativeLayout folder_layout;

        public BaseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Log.d(TAG, "BaseViewHolder: ");
            title = itemView.findViewById(getTitleId());
            detail = itemView.findViewById(getDetailId());
            thumbnail = itemView.findViewById(getThumbnailId());
            folder_layout = itemView.findViewById(getFolderLayoutId());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            highlight(v);
            callback.onItemClicked(getAdapterPosition());
        }
    }

    public int getFolderLayoutId(){
        return folderLayoutId;
    }

    public void setFolderLayoutId(int folderId){
        this.folderLayoutId = folderId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getDetailId() {
        return detailId;
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public void highlight(View view) {
        if (currView != null) {
            currView.setBackgroundColor(Color.TRANSPARENT);
        }
        currView = view;
        currView.setBackgroundColor(Color.DKGRAY);
    }

    private BaseListAdapter.IEntryClicked callback;

    public interface IEntryClicked {
        void onItemClicked(int position);
    }
}
