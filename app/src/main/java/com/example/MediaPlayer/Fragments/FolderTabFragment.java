package com.example.MediaPlayer.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.MediaPlayer.Activity.MainActivity;
import com.example.MediaPlayer.Adapter.FolderGridAdapter;
import com.example.MediaPlayer.Data.VideoRepository;
import com.example.MediaPlayer.Data.FolderRepository;
import com.example.MediaPlayer.Data.TreeNode;
import com.example.MediaPlayer.Data.MediaEntry;
import com.example.MediaPlayer.R;

import java.util.ArrayList;

public class FolderTabFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static String TAG = "FolderFragment";

    private TextView browse_by_folder;
    private LinearLayout arrow_layout;
    private TextView folder_title;
    private TextView play_in_all_folder;
    private GridView folder_and_video_gridview;


    private TreeNode<String> selected_folder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.folder_layout, container, false);
        setId(view);


        selected_folder = FolderRepository.getInstance().getRoot();
        setGridAdapter(selected_folder);

        folder_title.setText(changePathStyle(selected_folder.getPath()));

        play_in_all_folder.setOnClickListener(v -> {
            setPlay_in_all_folder();
        });

        arrow_layout.setOnClickListener(v -> {
            if (!selected_folder.isRoot()) {
                selected_folder = skipFolderParent(selected_folder.getParent());
                folder_title.setText(changePathStyle(selected_folder.getPath()));
                setGridAdapter(selected_folder);
            } else {
                getParentFragmentManager().popBackStack();
            }
        });

        folder_and_video_gridview.setOnItemClickListener(this);
        return view;
    }

    public void setPlay_in_all_folder() {
        if (selected_folder.getVideoChildren() != null) {
            ArrayList<Integer> videoPositions = getVideoIndexInFolder(selected_folder);
            ((MainActivity) getActivity()).getPLayList(videoPositions);
            ((MainLayoutFragment) getParentFragment()).onBackFromMiniPlayer();
        }
    }

    ArrayList<String> videoPathsInFolder = new ArrayList<>();

    public void getVideoPathsInFolder(TreeNode<String> folder) {

        if (!folder.getVideoChildren().isEmpty()) {
            for (TreeNode<String> video : folder.getVideoChildren()) {
                videoPathsInFolder.add(video.getPath().substring(video.getPath().lastIndexOf("/") + 1));
            }
        }
        if (!folder.getFolderChildren().isEmpty()) {
            for (TreeNode<String> f : folder.getFolderChildren()) {
                getVideoPathsInFolder(f);
            }
        }
    }

    public ArrayList<Integer> getVideoIndexInFolder(TreeNode<String> folder) {
        ArrayList<Integer> videoPositions = new ArrayList<>();

        getVideoPathsInFolder(folder);

        for (MediaEntry videoEntry : VideoRepository.getInstance().getVideoList()) {
            for (String videoPath : videoPathsInFolder) {

                if (videoEntry.getDisplay_name().equals(videoPath)) {
                    videoPositions.add(VideoRepository.getInstance().getVideoList().indexOf(videoEntry));
                }
            }
        }
        return videoPositions;
    }


    private ArrayList<TreeNode<String>> adapterData;

    public void setGridAdapter(TreeNode<String> path) {

        adapterData = new ArrayList<>();
        adapterData.addAll(path.getVideoChildren());
        adapterData.addAll(path.getFolderChildren());

        int switchPosition = path.getVideoChildren().size();

        FolderGridAdapter adapter = new FolderGridAdapter(getActivity(), adapterData, switchPosition);
        // Set the grid adapter
        folder_and_video_gridview.setAdapter(adapter);

    }

    public void setId(View v) {
        browse_by_folder = v.findViewById(R.id.browse_by_folder);
        arrow_layout = v.findViewById(R.id.arrow);
        folder_title = v.findViewById(R.id.folder_name);
        play_in_all_folder = v.findViewById(R.id.play_all_in_folder);
        folder_and_video_gridview = v.findViewById(R.id.folder_and_artist_gridview);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapterData.get(position).isLeaf()) {
            ((MainActivity) getActivity()).getVideo(adapterData.get(position).getVideoPosition());
            ((MainLayoutFragment) getParentFragment()).enterVideoPlayer();
        } else {
            // if folder only has 1 folder and no video, skip to its children
            selected_folder = skipFolderChild(adapterData.get(position));
            folder_title.setText(changePathStyle(selected_folder.getPath()));
            setGridAdapter(selected_folder);
        }

    }

    private TreeNode<String> skipFolderChild(TreeNode<String> folder) {
        while (folder.getVideoChildren().size() == 0
                && folder.getFolderChildren().size() == 1) {
            Log.d(TAG, "skipFolderChild: ");
            folder = folder.getFolderChildren().get(0);
        }
        return folder;
    }

    private TreeNode<String> skipFolderParent(TreeNode<String> folder) {
        while (folder.getVideoChildren().size() == 0
                && folder.getFolderChildren().size() == 1) {
            folder = folder.getParent();
        }
        return folder;
    }

    public String changePathStyle(String path) {
        return path.replace("/", " > ");
    }
}