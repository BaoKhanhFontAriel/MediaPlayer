package com.example.MediaPlayer.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.VideoListAdapter;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {
    private static final String TAG = "PlaylistFragment";


    private RecyclerView playlistRecycler;
    private TextView videoName;
    private TextView playlistTitle;

    private PlaylistViewModel playlistViewModel;
    private VideoListAdapter videoAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.playlist_layout, container, false);
        setId(view);

        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getCurrentPlaylist().observe(getViewLifecycleOwner(), currentPlaylist -> {
            Log.d(TAG, "getCurrentPlaylist observe: ");
            setupVideoRecycler((ArrayList<MediaEntry>) currentPlaylist);
            ((MainActivity) getActivity()).savePlaylist(currentPlaylist);
        });

        playlistViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), position -> {
            playlistRecycler.scrollToPosition(position);
            View itemView = playlistRecycler.getLayoutManager().findViewByPosition(position);
            if (itemView != null) {
                videoAdapter.highlight(itemView);
            } else handler.postDelayed(() -> {
                View anotherItemView = playlistRecycler.getLayoutManager().findViewByPosition(position);
                videoAdapter.highlight(anotherItemView);
            }, 5);
        });
        return view;
    }


    private void setId(View view) {
        playlistRecycler = view.findViewById(R.id.playlist_recycler);
        videoName = view.findViewById(R.id.video_name);
//        playlistTitle = view.findViewById(R.id.playlist_title);

    }

    public void setupVideoRecycler(ArrayList<MediaEntry> videoEntries) {
        videoAdapter = new VideoListAdapter(videoEntries, onClickVideoInPlaylist, playlistRecycler.getContext());
        playlistRecycler.setAdapter(videoAdapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(playlistRecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(playlistRecycler.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        playlistRecycler.addItemDecoration(horizontalDecoration);
        playlistRecycler.setLayoutManager(new LinearLayoutManager(playlistRecycler.getContext()));
    }

    VideoListAdapter.IEntryClicked onClickVideoInPlaylist = new VideoListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            playlistViewModel.getCurrentIndex().setValue(position);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        String playlistTitleNumber = "Playlist" + " (" + playlistViewModel.getCurrentPlaylist().getValue().size() + ")";
//        playlistTitle.setText(playlistTitleNumber);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
