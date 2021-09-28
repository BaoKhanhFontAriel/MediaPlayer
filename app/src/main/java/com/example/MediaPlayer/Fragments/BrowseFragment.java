package com.example.MediaPlayer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.VideoHistoryListAdapter;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.VideoHistoryRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.ArrayList;

public class BrowseFragment extends Fragment{
    public static final String TAG = "BrowseActivity";

    private LinearLayout backArrow;
    private TextView browse_more_files;
    private TextView recent_played;
    private TextView folder_text;
    private TextView artist_text;
    private ImageView folder_image;
    private ImageView artist_image;
    private RecyclerView history_layout;
    private LinearLayout folder_layout;
    private LinearLayout artist_layout;
    private PlaylistViewModel playlistViewModel;
    private Handler handler;


    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.browse_layout, container, false);
        setId(view);

        displayVideoHistory();

        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        folder_layout.setOnClickListener(v -> {
          ((MiniPlayerFragment) getParentFragment()).switchToFragment(new FolderFragment());
        });

        artist_layout.setOnClickListener(v -> {
            ((MiniPlayerFragment) getParentFragment()).switchToFragment(new ArtistFragment());
        });

        return view;
    }


    public void setId(View view) {
        recent_played = view.findViewById(R.id.recent_played);
        folder_image = view.findViewById(R.id.folder);
        artist_image = view.findViewById(R.id.artist_image);
        folder_text = view.findViewById(R.id.folder_text);
        artist_text = view.findViewById(R.id.artist_text);
        artist_layout = view.findViewById(R.id.artist_layout);
        folder_layout = view.findViewById(R.id.folder_layout);
        history_layout = view.findViewById(R.id.history);
    }


    private void displayVideoHistory(){
        if (VideoHistoryRepository.getInstance().getHistory() != null){
            recent_played.setVisibility(View.VISIBLE);
            history_layout.setVisibility(View.VISIBLE);

            VideoHistoryListAdapter videoGridAdapter =
                    new VideoHistoryListAdapter((ArrayList<MediaEntry>) VideoHistoryRepository.getInstance().getHistory(), clicked, getContext());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
            linearLayoutManager.setStackFromEnd(true);
            history_layout.setLayoutManager(linearLayoutManager);
            history_layout.setAdapter(videoGridAdapter);
        }
        else {
            Log.d(TAG, "GONE: ");
            recent_played.setVisibility(View.GONE);
            history_layout.setVisibility(View.GONE);
        }
    }

    VideoHistoryListAdapter.IEntryClicked clicked = position -> {
        Log.d(MainActivity.TAG, "onItemClicked: ");

        MediaEntry video = VideoHistoryRepository.getInstance().getHistory().get(position);
        VideoHistoryRepository.getInstance().getHistory().remove(position);
        int videoPosition = 0;
        for (MediaEntry v: VideoRepository.getInstance().getVideoList()){
            if (v.getDisplay_name().equals(video.getDisplay_name())){
                videoPosition = VideoRepository.getInstance().getVideoList().indexOf(v);
            }
        }

        ((MainActivity) getActivity()).getVideo(videoPosition);
        ((MiniPlayerFragment) getParentFragment()).enterVideoPlayer();
    };

    public LinearLayout getArrow(){
        return backArrow;
    }
}