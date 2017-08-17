package com.dmss.dmssevents.models;

import java.util.ArrayList;

/**
 * Created by jaya.krishna on 3/12/2017.
 */
public class AlbumDetailsModel {
    String albumName, albumCoverPhotoUrl;
    ArrayList<PhotoDetailsModel> photoDetailsArray = new ArrayList<PhotoDetailsModel>();

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCoverPhotoUrl() {
        return albumCoverPhotoUrl;
    }

    public void setAlbumCoverPhotoUrl(String albumCoverPhotoUrl) {
        this.albumCoverPhotoUrl = albumCoverPhotoUrl;
    }

    public ArrayList<PhotoDetailsModel> getPhotoDetailsArray() {
        return photoDetailsArray;
    }

    public void setPhotoDetailsArray(ArrayList<PhotoDetailsModel> photoDetailsArray) {
        this.photoDetailsArray = photoDetailsArray;
    }
}
