package com.example.MediaPlayer.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.VideoListAdapter;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VideoTabFragment extends Fragment {
    private PlaylistFragment playlistFragment;
    private MediaPlayerViewModel mediaPlayerViewModel;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            playlistFragment = new PlaylistFragment();
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayerViewModel = new ViewModelProvider(getActivity()).get(MediaPlayerViewModel.class);
        setId(view);
        setUpRecyclerView();
    }

    public void setId(View view) {
        recyclerView = view.findViewById(R.id.playlist_recycler);
    }

    public void setUpRecyclerView() {
        VideoListAdapter adapter = new VideoListAdapter((ArrayList<MediaEntry>) VideoRepository.getInstance().getVideoList(), onClickVideoInPlaylist, getContext());
        recyclerView.setAdapter(adapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    BaseListAdapter.IEntryClicked onClickVideoInPlaylist = position -> {
        mediaPlayerViewModel.getCurrentPlaylist().setValue(VideoRepository.getInstance().getVideoList());
        mediaPlayerViewModel.getCurrentIndex().setValue(position);
        PlayingSong.getInstance().getMediaPlayer().stop();
        mediaPlayerViewModel.getIsSong().setValue(false);
        ((MainActivity) getActivity()).enterVideoPlayer();
    };
}
