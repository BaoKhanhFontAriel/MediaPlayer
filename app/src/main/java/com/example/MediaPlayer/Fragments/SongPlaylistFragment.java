package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SongPlaylistFragment extends Fragment {
    private static final String TAG = "SongPlaylistFragment";
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.playlist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        setHasOptionsMenu(true);
        initView(view);
        setUpButtonBackPressed();
        setUpViewModel();
    }

    public void setUpButtonBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ((MainLayoutFragment) getParentFragment().getParentFragment()).showNavigationBar();
                ((MainLayoutFragment) getParentFragment()).hideAlbumSongsFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private MediaPlayerViewModel mediaPlayerViewModel;
    public void setUpViewModel(){
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
        mediaPlayerViewModel.getCurrentPlaylist().observe(getViewLifecycleOwner(), new Observer<List<MediaEntry>>() {
            @Override
            public void onChanged(List<MediaEntry> mediaEntries) {
                Log.d(TAG, "onChanged: ");
                TrackListAdapter trackListAdapter = new TrackListAdapter((ArrayList<MediaEntry>) mediaEntries,
                        clicked, getContext());
                recyclerView.setAdapter(trackListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            }
        });
    }

    public void initView (View view ){
        recyclerView = view.findViewById(R.id.playlist_recycler);
    }

    BaseListAdapter.IEntryClicked clicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            mediaPlayerViewModel.getCurrentIndex().setValue(position);
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        Log.d("TAG", "onOptionsItemSelected: " + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            ((MainLayoutFragment) getParentFragment()).showNavigationBar();
            ((MainLayoutFragment) getParentFragment()).hideAlbumSongsFragment();
        }
        return true;
    }
}
