package com.example.MediaPlayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.AlbumListAdapter;
import com.example.MediaPlayer.Adapter.BaseListAdapter;
import com.example.MediaPlayer.Data.AlbumRepository;
import com.example.MediaPlayer.R;
import com.example.MediaPlayer.ViewModel.MediaPlayerViewModel;

import org.jetbrains.annotations.NotNull;

public class AlbumTabFragment extends Fragment {
    private static final String TAG = "AlbumFragment";

    MediaPlayerViewModel mediaPlayerViewModel;
    private RecyclerView recyclerView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
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
        recyclerView = view.findViewById(R.id.playlist_recycler);
    }

    private void setRecyclerView() {
        AlbumListAdapter adapter = new AlbumListAdapter(clicked, getContext(), AlbumRepository.getInstance().getAlbumList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    BaseListAdapter.IEntryClicked clicked = new BaseListAdapter.IEntryClicked() {
        @Override
        public void onItemClicked(int position) {
            mediaPlayerViewModel.getAlbumEntryMutableLiveData().setValue(AlbumRepository.getInstance().getAlbumList().get(position));
            ((MainLayoutFragment) getParentFragment().getParentFragment()).hideNavigationBar();
            ((MainActivity) getActivity()).showBackButton();
            ((MainLayoutFragment) getParentFragment().getParentFragment()).showAlbumSongsFragment();
        }
    };



}
