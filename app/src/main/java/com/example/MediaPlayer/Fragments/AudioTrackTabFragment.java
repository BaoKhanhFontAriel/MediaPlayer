package com.example.MediaPlayer.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.TrackListAdapter;
import com.example.MediaPlayer.Data.AudioRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.PlaylistViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AudioTrackTabFragment extends Fragment {
    RecyclerView recyclerView;
    TextView audioName;
    TrackListAdapter trackListAdapter;
    PlaylistViewModel playlistViewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setId(view);
        setupVideoRecycler(AudioRepository.getInstance().getAudioList());
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
    }


    private void setId(View view) {
        recyclerView = view.findViewById(R.id.playlist_recycler);
        audioName = view.findViewById(R.id.media_name_mini_song_player);
//        playlistTitle = view.findViewById(R.id.playlist_title);

    }

    public void setupVideoRecycler(ArrayList<MediaEntry> audioEntries) {
        trackListAdapter = new TrackListAdapter(audioEntries, onClickVideoInPlaylist, recyclerView.getContext());
        recyclerView.setAdapter(trackListAdapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    TrackListAdapter.IEntryClicked onClickVideoInPlaylist = new TrackListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            playlistViewModel.getIsPauseSelected().setValue(false);
            playlistViewModel.getCurrentPlaylist().setValue(AudioRepository.getInstance().getAudioList());
            playlistViewModel.getCurrentIndex().setValue(position);
            playlistViewModel.getIsSong().setValue(true);
            ((MainActivity)getActivity()).enterSongPlayer();
        }
    };
}
