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
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.GenreListAdapter;
import com.example.MediaPlayer.Data.GenreRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

public class GenreTabFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    private TextView genre_title;
    private TextView numbers_of_songs;
    private RecyclerView recyclerView;
    MediaPlayerViewModel mediaPlayerViewModel;

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
        setRecyclerView();
        mediaPlayerViewModel = new ViewModelProvider(requireActivity()).get(MediaPlayerViewModel.class);
    }

    private void setId(View view) {
        genre_title = view.findViewById(R.id.genre);
        numbers_of_songs = view.findViewById(R.id.number_of_songs);
        recyclerView = view.findViewById(R.id.playlist_recycler);
    }

    private void setRecyclerView() {
        GenreListAdapter adapter = new GenreListAdapter(GenreRepository.getInstance().getGenreList(), clicked, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    BaseListAdapter.IEntryClicked clicked = position -> {
        mediaPlayerViewModel.getGenreSongEntryMutableLiveData().setValue(GenreRepository.getInstance().getGenreList().get(position).getSongs_within());
        ((MainLayoutFragment) getParentFragment().getParentFragment()).hideNavigationBar();
        ((MainActivity) getActivity()).showBackButton();
        ((MainLayoutFragment) getParentFragment().getParentFragment()).showGenreSongsFragment();
    };
}
