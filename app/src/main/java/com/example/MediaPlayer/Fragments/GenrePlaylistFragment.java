package com.example.MediaPlayer.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.TrackListAdapter;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import java.util.ArrayList;
import java.util.List;

public class GenrePlaylistFragment extends Fragment {
    private static final  String TAG = "GenrePlaylistFragment";
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.genre_song_playlist_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.genre_playlist_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        recyclerView = view.findViewById(R.id.playlist_recycler);

        MediaPlayerViewModel mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
        mediaPlayerViewModel.getGenreSongEntryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<MediaEntry>>() {
            @Override
            public void onChanged(List<MediaEntry> mediaEntries) {
                TrackListAdapter trackListAdapter = new TrackListAdapter((ArrayList<MediaEntry>) mediaEntries, callback, getContext());
                recyclerView.setAdapter(trackListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainLayoutFragment) getParentFragment()).showNavigationBar();
                ((MainLayoutFragment) getParentFragment()).hideGenreSongsFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    BaseListAdapter.IEntryClicked callback = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        if (item.getItemId() == android.R.id.home){
            ((MainLayoutFragment) getParentFragment()).showNavigationBar();
            ((MainLayoutFragment) getParentFragment()).hideGenreSongsFragment();
        }
        return true;
    }
}
