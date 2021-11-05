package com.example.MediaPlayer.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.MusicArtistListAdapter;
import com.example.MediaPlayer.Data.SongArtistRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArtistTabFragment extends Fragment {
    private static final String TAG = "ArtistFragment";

    private TextView browse_by_artist;
    private LinearLayout arrow_button;
    private RecyclerView recyclerView;
    private ArrayList<String> artists = new ArrayList<>();
    MediaPlayerViewModel mediaPlayerViewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initViewModel();
        setUpRecyvclerView();
    }

    private void initView(View view) {
        browse_by_artist = view.findViewById(R.id.browse_by_artists);
        arrow_button = view.findViewById(R.id.arrow);
        recyclerView = view.findViewById(R.id.playlist_recycler);
    }

    private void initViewModel(){
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
    }

    private void setUpRecyvclerView() {
        MusicArtistListAdapter adapter = new MusicArtistListAdapter(SongArtistRepository.getInstance().getArtistList(), clicked, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    BaseListAdapter.IEntryClicked clicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            mediaPlayerViewModel.getArtistName().setValue(SongArtistRepository.getInstance().getArtistList().get(position).getArtist());
            ((MainLayoutFragment) getParentFragment().getParentFragment()).hideNavigationBar();
            ((MainLayoutFragment) getParentFragment().getParentFragment()).showSongArtistFragment();

        }
    };
}
