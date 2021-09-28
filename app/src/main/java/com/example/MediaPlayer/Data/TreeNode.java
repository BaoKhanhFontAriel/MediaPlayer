package com.example.MediaPlayer.Data;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private static String TAG = "PathTree";

    private T path = null;
    private TreeNode<T> parent = null;
    private List<TreeNode<T>> folderChildren = new ArrayList<>();
    private List<TreeNode<T>> videoChildren = new ArrayList<>();
    private int videoPosition;

    public TreeNode(T path) {
        this.path = path;
    }

    public TreeNode(T path, TreeNode<T> parent) {
        this.path = path;
        this.parent = parent;
    }

    public void addAllFolder(List<TreeNode<T>> mFolderChildren){
        this.folderChildren = mFolderChildren;
    }

    public void addFolderChild(T path) {
        TreeNode<T> child = new TreeNode<T>(path);
        this.folderChildren.add(child);

    }

    public void addFolderChild(TreeNode<T> folder) {
        this.folderChildren.add(folder);
    }

    public void addVideoChild(T path){
        TreeNode<T> child = new TreeNode<T>(path);
        this.videoChildren.add(child); 
    }


    public void addVideoChild(TreeNode<T> folder) {
        this.videoChildren.add(folder);
    }

    public T getPath() {
        return path;
    }

    public void setPath(T path) {
        this.path = path;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getVideoChildren() {
        return videoChildren;
    }

    public List<TreeNode<T>> getFolderChildren() {
        return folderChildren;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public void setVideoPosition(int position){ this.videoPosition = position;}

    public boolean isLeaf() {
        return (this.folderChildren.size() == 0 && this.videoChildren.size() == 0);
    }

    public int getVideoPosition() {
        return this.videoPosition;
    }
}
