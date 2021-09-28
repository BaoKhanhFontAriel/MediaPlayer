package com.example.MediaPlayer.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ParcelableVideoList implements Parcelable {
    List<MediaEntry> videoEntries = new ArrayList<>();

    public ParcelableVideoList(Parcel in) {
        in.readTypedList(videoEntries, MediaEntry.CREATOR);
    }

    public ParcelableVideoList(List<MediaEntry> videoEntries){
        this.videoEntries = videoEntries;
    }

    public static final Creator<ParcelableVideoList> CREATOR = new Creator<ParcelableVideoList>() {
        @Override
        public ParcelableVideoList createFromParcel(Parcel in) {
            return new ParcelableVideoList(in);
        }

        @Override
        public ParcelableVideoList[] newArray(int size) {
            return new ParcelableVideoList[size];
        }
    };

    public List<MediaEntry> getVideoEntries(){
        return videoEntries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.videoEntries);
    }
}
