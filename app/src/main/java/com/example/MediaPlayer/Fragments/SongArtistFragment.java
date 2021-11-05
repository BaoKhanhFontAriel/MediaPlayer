package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Adapter.AlbumListAdapter;
import com.example.MediaPlayer.Adapter.AlbumSongsListAdapter;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.SongArtistAlbumListAdapter;
import com.example.MediaPlayer.Data.AlbumRepository;
import com.example.MediaPlayer.Data.SongRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

public class SongArtistFragment extends Fragment {
    private RecyclerView recyclerViewAlbum;
    private RecyclerView recyclerViewSong;
    private TextView artist;
    MediaPlayerViewModel mediaPlayerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.scrollview_song_artist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        setUpViewModel();
        initBackPressed();
    }

    private void initView(View view){
        recyclerViewAlbum = view.findViewById(R.id.recyclerView_album_artist_frag);
        recyclerViewSong = view.findViewById(R.id.recyclerView_song_artist_frag);
        artist = view.findViewById(R.id.artist_artist_frag);
    }

    private void setUpViewModel(){
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getArtistName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String artistName) {
                artist.setText(artistName);
                initAlbumRecyclerView(artistName);
                initSongRecyclerView(artistName);
            }
        });
    }

    private void initAlbumRecyclerView(String artistName){
        SongArtistAlbumListAdapter albumListAdapter = new SongArtistAlbumListAdapter(
                AlbumRepository.getInstance().filterArtist(getContext(), artistName),
                albumClicked,
                getContext());

        recyclerViewAlbum.setAdapter(albumListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewAlbum.setLayoutManager(linearLayoutManager);
    }

    private void  initSongRecyclerView(String artistName){
        AlbumSongsListAdapter albumSongsListAdapter = new AlbumSongsListAdapter(
                SongRepository.getInstance().getFilteredAudio(getContext(), MediaStore.Audio.Media.ARTIST, artistName),
                songClicked,
                getContext());
        recyclerViewSong.setAdapter(albumSongsListAdapter);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ((MainLayoutFragment) getParentFragment()).showNavigationBar();
                ((MainLayoutFragment) getParentFragment()).hideSongArtistFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    BaseListAdapter.IEntryClicked albumClicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {

        }
    };

    BaseListAdapter.IEntryClicked songClicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((MainLayoutFragment) getParentFragment()).showNavigationBar();
            ((MainLayoutFragment) getParentFragment()).hideSongArtistFragment();
        }
        return true;
    }

}
