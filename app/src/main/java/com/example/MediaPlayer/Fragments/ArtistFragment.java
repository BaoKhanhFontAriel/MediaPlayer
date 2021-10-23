package com.example.MediaPlayer.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Adapter.MusicArtistListAdapter;
import com.example.MediaPlayer.Data.AudioArtistRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArtistFragment extends Fragment {
    private static final String TAG = "ArtistFragment";

    private TextView browse_by_artist;
    private LinearLayout arrow_button;
    private RecyclerView artistGridView;
    private ArrayList<String> artists = new ArrayList<>();
    MediaPlayerViewModel mediaPlayerViewModel;
    public static final String ARG_OBJECT = "object";

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.playlist_layout, container, false);
        setId(view);
//        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        setUpArtistView();

//        arrow_button.setOnClickListener(v -> {
//            getParentFragmentManager().popBackStack("fragmentBrowse", 0);
//        });

        return view;
    }

    private void setId(View view) {
        browse_by_artist = view.findViewById(R.id.browse_by_artists);
        arrow_button = view.findViewById(R.id.arrow);
        artistGridView = view.findViewById(R.id.playlist_recycler);
    }

    private void setUpArtistView() {
        MusicArtistListAdapter adapter = new MusicArtistListAdapter(AudioArtistRepository.getInstance().getArtistList(), clicked, getActivity());
        artistGridView.setLayoutManager(new LinearLayoutManager(getContext()));
        artistGridView.setAdapter(adapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(artistGridView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(artistGridView.getContext(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        artistGridView.addItemDecoration(horizontalDecoration);

        artistGridView.setLayoutManager(new LinearLayoutManager(artistGridView.getContext()));
    }

    BaseListAdapter.IEntryClicked clicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
//        playlistViewModel.getArtistName().setValue(artists.get(position));
//        ((MiniPlayerFragment) getParentFragment()).switchToFragment(new VideoByArtistFragment());
        }
    };

    private Handler handler;
}
