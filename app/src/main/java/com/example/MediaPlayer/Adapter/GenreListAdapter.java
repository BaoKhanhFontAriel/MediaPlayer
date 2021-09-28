package com.example.MediaPlayer.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.MediaPlayer.Data.GenreEntry;
import com.example.MediaPlayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenreListAdapter extends BaseListAdapter {
    private List<GenreEntry> genreList = new ArrayList<>();

    public GenreListAdapter(List<GenreEntry> genreList, IEntryClicked callback, Context context) {
        super(callback, context);
        this.genreList = genreList;
        setLayoutId(R.layout.genre_ui);
        setTitleId(R.id.genre);
        setDetailId(R.id.number_of_songs);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        GenreEntry entry = genreList.get(position);
        holder.title.setText(entry.getGenre());

        int num_songs = entry.getSongs_within().size();
        String text = "";
        switch (num_songs) {
            case 0:
                return;
            case 1:
                text = 1 + " song";
                holder.detail.setText(text);
                return;
            default:
                text = num_songs + " songs";
                holder.detail.setText(text);
        }
    }


    @Override
    public int getItemCount() {
        return genreList.size();
    }
}
