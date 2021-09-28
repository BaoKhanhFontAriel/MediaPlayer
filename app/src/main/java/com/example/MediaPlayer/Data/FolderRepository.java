package com.example.MediaPlayer.Data;

import java.util.ArrayList;

public class FolderRepository {
    private static String TAG = "PathTree";
    private ArrayList<String> rootPaths = new ArrayList<>();

    private static FolderRepository instance;

    public static FolderRepository getInstance() {
        if (instance == null) {
            instance = new FolderRepository();
        }
        return instance;
    }

    public void makeFolderTree() {
        addVideoToFolder();
        createFolders();
        addChildFolderToParentFolder(getParentFolders());
    }


    public void addVideoToFolder() {
        ArrayList<TreeNode<String>> parents = new ArrayList<>();
        ArrayList<String> paths = new ArrayList<>();


        for (int i = 0; i < VideoRepository.getInstance().getVideoList().size(); i++) {

            MediaEntry videoEntry = VideoRepository.getInstance().getVideoList().get(i);


            String fullPath = videoEntry.getVolumeName() + "/" + videoEntry.getPath() + videoEntry.getDisplay_name();

            TreeNode<String> video = new TreeNode<>(fullPath);
            video.setVideoPosition(i);

            String parentPath = fullPath.substring(0, fullPath.lastIndexOf("/"));

            TreeNode<String> parentFolder = new TreeNode<>(parentPath);

            if (!rootPaths.contains(videoEntry.getVolumeName())) {
                rootPaths.add(videoEntry.getVolumeName());
                TreeNode<String> rootChild = new TreeNode<>(videoEntry.getVolumeName());

                root.addFolderChild(rootChild);
                rootChild.setParent(root);
            }

            if (!paths.contains(parentPath)) {
                parents.add(parentFolder);
                paths.add(parentPath);
            }


            parents.get(paths.indexOf(parentPath)).addVideoChild(video);
            video.setParent(parents.get(paths.indexOf(parentPath)));
        }

        setParentFolder(parents);
    }


    public void createFolders() {
        ArrayList<TreeNode<String>> folderList = new ArrayList<>(getParentFolders());
        ArrayList<String> paths = new ArrayList<>();

        for (TreeNode<String> folder : folderList) {
            paths.add(folder.getPath());
        }

        for (TreeNode<String> folder : getParentFolders()) {

            String parentPath = folder.getPath().substring(0, folder.getPath().lastIndexOf("/"));

            while (!rootPaths.contains(parentPath)) {
                if (!paths.contains(parentPath)) {
                    paths.add(parentPath);

                    folderList.add(new TreeNode<>(parentPath));
                }

                parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
            }

        }
        folderList.addAll(root.getFolderChildren());
        setParentFolder(folderList);
    }


    public void addChildFolderToParentFolder(ArrayList<TreeNode<String>> folders) {
        ArrayList<TreeNode<String>> parents = new ArrayList<>();
        ArrayList<TreeNode<String>> foldersNotHaveParent = new ArrayList<>();

        ArrayList<String> paths = new ArrayList<>();

        for (TreeNode<String> folder : folders) {
            for (TreeNode<String> f : folders) {
                if (!rootPaths.contains(f.getPath())) {
                    String parentPath = f.getPath().substring(0, f.getPath().lastIndexOf("/"));

                    if (parentPath.equals(folder.getPath())) {

                        if (!paths.contains(folder.getPath())) {
                            parents.add(folder);
                            paths.add(folder.getPath());
                        }


                        parents.get(paths.indexOf(folder.getPath())).addFolderChild(f);
                        f.setParent(parents.get(paths.indexOf(folder.getPath())));

                    }
                }
            }
        }

        for (TreeNode<String> folder : folders) {
            if (folder.getParent() == null) {
                foldersNotHaveParent.add(folder);
            }
        }

        setParentFolder(foldersNotHaveParent);
    }


    private TreeNode<String> root = new TreeNode<>("Storage");


    ArrayList<TreeNode<String>> folders;

    public void setParentFolder(ArrayList<TreeNode<String>> parents) {
        folders = parents;
    }

    public ArrayList<TreeNode<String>> getParentFolders() {
        return folders;
    }

    public TreeNode<String> getRoot() {
        return root;
    }
}



