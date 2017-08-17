package com.dmss.dmssevents.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 21-03-2017.
 */
public class AlbumsModel {
    int id, eventId, createdBy, modifiedBy, photosCount;
    String albumName = "", createdOn = "", modifiedOn = "", dMSSEmployee = "";
    boolean isActive;
    ArrayList<PhotoDetailsModel> albumImageList = new ArrayList<PhotoDetailsModel>();

    public AlbumsModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                eventId = resultJson.isNull("EventId") ? 0 : resultJson.getInt("EventId");
                createdBy = resultJson.isNull("CreatedBy") ? 0 : resultJson.getInt("CreatedBy");
                modifiedBy = resultJson.isNull("ModifiedBy") ? 0 : resultJson.getInt("ModifiedBy");
                albumName = resultJson.isNull("AlbumName") ? "" : resultJson.getString("AlbumName");
                createdOn = resultJson.isNull("CreatedOn") ? "" : resultJson.getString("CreatedOn");
                modifiedOn = resultJson.isNull("ModifiedOn") ? "" : resultJson.getString("ModifiedOn");
                dMSSEmployee = resultJson.isNull("DMSSEmployee") ? "" : resultJson.getString("DMSSEmployee");
                isActive = resultJson.isNull("IsActive") ? false : resultJson.getBoolean("IsActive");
                if (!resultJson.isNull("AlbumImageList")) {
                    JSONArray imagesListJson = resultJson.getJSONArray("AlbumImageList");
                    if (imagesListJson.length() > 0) {
                        for (int i = 0; i < imagesListJson.length(); i++) {
                            PhotoDetailsModel photoDetailsModel = new PhotoDetailsModel(imagesListJson.getJSONObject(i));
                            albumImageList.add(photoDetailsModel);
                        }
                    }
                }
                photosCount = albumImageList.size();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getDMSSEmployee() {
        return dMSSEmployee;
    }

    public void setDMSSEmployee(String DMSSEmployee) {
        this.dMSSEmployee = DMSSEmployee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ArrayList<PhotoDetailsModel> getAlbumImageList() {
        return albumImageList;
    }

    public void setAlbumImageList(ArrayList<PhotoDetailsModel> albumImageList) {
        this.albumImageList = albumImageList;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public String getdMSSEmployee() {
        return dMSSEmployee;
    }

    public void setdMSSEmployee(String dMSSEmployee) {
        this.dMSSEmployee = dMSSEmployee;
    }
}
