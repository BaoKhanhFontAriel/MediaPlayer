package com.example.MediaPlayer.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.AlbumSongsListAdapter;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Data.AlbumEntry;
import com.example.MediaPlayer.Data.AudioRepository;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.Data.Utils;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlbumSongsFragment extends Fragment {
    private static final String TAG = "AlbumSongsFragment";

    MediaPlayerViewModel mediaPlayerViewModel;
    private TextView album_name;
    private TextView album_artist_name;
    private ImageView album_thumbnail;
    private RecyclerView recyclerView;
    private List<MediaEntry> songsWithin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scrollview_album_songs_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
        mediaPlayerViewModel.getAlbumEntryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<AlbumEntry>() {
            @Override
            public void onChanged(AlbumEntry albumEntry) {
                album_name.setText(albumEntry.getAlbum());
                album_artist_name.setText(albumEntry.getArtist());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    album_thumbnail.setImageBitmap(Utils.getThumbnail(getContext(), albumEntry.getUri(), 521, 384));
                }

                songsWithin = AudioRepository.getInstance().getFilteredAudio(getContext(),
                        MediaStore.Audio.Media.ALBUM,
                        albumEntry.getAlbum());

                Log.d(TAG, "songsWithin: " + songsWithin.size());

                AlbumSongsListAdapter albumSongsListAdapter = new AlbumSongsListAdapter(
                        songsWithin, clicked, getContext());

                recyclerView.setAdapter(albumSongsListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).hideBackButton();
                ((MainLayoutFragment) getParentFragment()).showNavigationBar();
                ((MainLayoutFragment) getParentFragment()).hideAlbumSongsFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    public void initView(View view){
        album_name = view.findViewById(R.id.album_name_album_frag);
        album_artist_name = view.findViewById(R.id.album_artist_name);
        recyclerView = view.findViewById(R.id.recyclerview_album);
        album_thumbnail = view.findViewById(R.id.album_thumbnail);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        Log.d("TAG", "onOptionsItemSelected: " + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            ((MainActivity)getActivity()).hideBackButton();
            ((MainLayoutFragment) getParentFragment()).showNavigationBar();
            ((MainLayoutFragment) getParentFragment()).hideAlbumSongsFragment();
        }
        return true;
    }

    BaseListAdapter.IEntryClicked clicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            mediaPlayerViewModel.getCurrentPlaylist().setValue(songsWithin);
            mediaPlayerViewModel.getCurrentIndex().setValue(position);
            mediaPlayerViewModel.getIsSong().setValue(true);
            ((MainLayoutFragment) getParentFragment()).showMiniSongPlayer();
        }
    };


}
