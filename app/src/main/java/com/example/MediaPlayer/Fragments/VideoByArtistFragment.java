package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.VideoGridAdapter;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import java.util.ArrayList;

public class VideoByArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final static String TAG = "VideoByArtistFragment";

    private TextView video_by_artist;
    private LinearLayout arrow;
    private TextView play_all;
    private ImageView artist_icon;
    private GridView videoGrid;
    private ArrayList<MediaEntry> videosByArtist = new ArrayList<>();
    PlaylistViewModel playlistViewModel;
    private Handler handler;

    public VideoByArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_by_video_layout, container, false);
        setId(view);
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getArtistName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String artistName) {
                for (MediaEntry videoEntry : VideoRepository.getInstance().getVideoList()) {
                    if (videoEntry.getArtistName().equals(artistName)) {
                        videosByArtist.add(videoEntry);
                    }
                }
                String text = "Video by " + artistName;
                video_by_artist.setText(text);
                VideoGridAdapter adapter = new VideoGridAdapter(getActivity(), videosByArtist);
                videoGrid.setAdapter(adapter);
            }
        });

        arrow.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        play_all.setOnClickListener(v -> {
            setPlay_all();
        });

        videoGrid.setOnItemClickListener(this);
        return view;
    }


    private void setId(View view) {
        video_by_artist = view.findViewById(R.id.video_by_artists);
        arrow = view.findViewById(R.id.arrow);
        play_all = view.findViewById(R.id.play_all);
        artist_icon = view.findViewById(R.id.artist_icon);
        videoGrid = view.findViewById(R.id.artist_gridview);
    }
    private void setPlay_all(){
        ArrayList<Integer> videoPositions = new ArrayList<>();

        for (MediaEntry video : videosByArtist) {
            videoPositions.add(
                    VideoRepository.getInstance().getVideoList().indexOf(video));
        }

        ((MainActivity) getActivity()).getPLayList(videoPositions);
        ((MiniPlayerFragment) getParentFragment()).onBackFromMiniPlayer();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int videoPosition = VideoRepository.getInstance().getVideoList().indexOf(videosByArtist.get(position));
        ((MainActivity) getActivity()).getVideo(videoPosition);
        ((MiniPlayerFragment) getParentFragment()).enterVideoPlayer();
    }
}
