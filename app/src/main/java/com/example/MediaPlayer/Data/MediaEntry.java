package com.example.MediaPlayer.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaEntry implements Parcelable {

    public static final Parcelable.Creator<MediaEntry> CREATOR = new Parcelable.Creator<MediaEntry>() {
        public MediaEntry createFromParcel(Parcel in) {
            return new MediaEntry(in);
        }

        public MediaEntry[] newArray(int size) {
            return new MediaEntry[size];
        }
    };

    private long id;
    private String uri;
    private String display_name;
    private String path;
    private String MediaName;
    private String artistName;
    private String album;
    private int duration;
    private String volumeName;

    public MediaEntry(long id, String uri, String display_name, String path, String videoName, String artistName, String album, int duration, String volumeName) {
        this.id = id;
        this.uri = uri;
        this.display_name = display_name;
        this.path = path;
        this.MediaName = videoName;
        this.artistName = artistName;
        this.album = album;
        this.duration = duration;
        this.volumeName = volumeName;
    }

    public String getMediaName() {
        return MediaName;
    }

    public int getDuration() {
        return duration;
    }

    public String getUri() {
        return uri;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public long getId() {
        return id;
    }

    public MediaEntry(Parcel in){
        this.id = in.readLong();
        this.uri = in.readString();
        this.display_name =  in.readString();
        this.path = in.readString();
        this.MediaName = in.readString();
        this.artistName = in.readString();
        this.album = in.readString();
        this.duration = in.readInt();
        this.volumeName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.uri);
        dest.writeString(this.display_name);
        dest.writeString(this.path);
        dest.writeString(this.MediaName);
        dest.writeString( this.artistName);
        dest.writeString(this.album);
        dest.writeInt(this.duration);
        dest.writeString(this.volumeName);
    }
}
