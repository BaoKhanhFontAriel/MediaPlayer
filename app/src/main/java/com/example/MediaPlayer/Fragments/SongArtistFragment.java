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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.AlbumListAdapter;
import com.example.MediaPlayer.Adapter.AlbumSongsListAdapter;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.SongArtistAlbumListAdapter;
import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.Data.AlbumRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.SongRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SongArtistFragment extends Fragment {
    private RecyclerView recyclerViewAlbum;
    private RecyclerView recyclerViewSong;
    private TextView artist;
    private ArrayList<AlbumEntry> albumsList;
    private ArrayList<MediaEntry> songsList;
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
        initToolbar(view);
        setUpViewModel();
        initBackPressed();
    }

    public void getAlbums(String artistName){
       albumsList = AlbumRepository.getInstance().filterArtist(getContext(), artistName);
    }

    public void getSongs(String artistName){
        songsList = (ArrayList<MediaEntry>) SongRepository.getInstance().getFilteredAudio(getContext(),
                MediaStore.Audio.Media.ARTIST, artistName);
    }

    private void initView(View view){
        recyclerViewAlbum = view.findViewById(R.id.recyclerView_album_artist_frag);
        recyclerViewSong = view.findViewById(R.id.recyclerView_song_artist_frag);
        artist = view.findViewById(R.id.artist_artist_frag);
    }

    private void initToolbar(View view){
        Toolbar toolbar =  view.findViewById(R.id.toolbar_song_artist_layout);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
    }

    private void setUpViewModel(){
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);

        mediaPlayerViewModel.getArtistName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String artistName) {
                artist.setText(artistName);
                getAlbums(artistName);
                getSongs(artistName);
                initAlbumRecyclerView();
                initSongRecyclerView();
            }
        });
    }

    private void initAlbumRecyclerView(){
        SongArtistAlbumListAdapter albumListAdapter = new SongArtistAlbumListAdapter(
                albumsList,
                albumClicked,
                getContext());

        recyclerViewAlbum.setAdapter(albumListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewAlbum.setLayoutManager(linearLayoutManager);
    }

    private void  initSongRecyclerView(){
        AlbumSongsListAdapter albumSongsListAdapter = new AlbumSongsListAdapter(
                songsList,
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

    BaseListAdapter.IEntryClicked albumClicked = position -> {
        mediaPlayerViewModel.getAlbumEntryMutableLiveData().setValue(albumsList.get(position));
        ((MainLayoutFragment) getParentFragment()).showAlbumSongsFragment();
    };

    BaseListAdapter.IEntryClicked songClicked = position -> {
        mediaPlayerViewModel.getCurrentPlaylist().setValue(songsList);
        mediaPlayerViewModel.getCurrentIndex().setValue(position);
        mediaPlayerViewModel.getIsSong().setValue(true);
        ((MainActivity)getActivity()).showSongPlayerFragment();
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
