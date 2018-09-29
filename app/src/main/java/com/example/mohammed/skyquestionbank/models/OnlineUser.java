package com.example.mohammed.skyquestionbank.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OnlineUser implements Parcelable {

    public static final Creator<OnlineUser> CREATOR = new Creator<OnlineUser>() {
        @Override
        public OnlineUser createFromParcel(Parcel in) {
            return new OnlineUser(in);
        }

        @Override
        public OnlineUser[] newArray(int size) {
            return new OnlineUser[size];
        }
    };
    private String playerId;
    private String imgUrl;
    private boolean isPlaying;
    private String userName;
    private String uid;

    public OnlineUser() {

    }

    protected OnlineUser(Parcel in) {
        playerId = in.readString();
        imgUrl = in.readString();
        isPlaying = in.readByte() != 0;
        userName = in.readString();
        uid = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerId);
        dest.writeString(imgUrl);
        dest.writeByte((byte) (isPlaying ? 1 : 0));
        dest.writeString(userName);
        dest.writeString(uid);
    }
}
