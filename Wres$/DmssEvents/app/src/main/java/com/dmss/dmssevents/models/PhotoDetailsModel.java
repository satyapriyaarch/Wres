package com.dmss.dmssevents.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jaya.krishna on 3/12/2017.
 */
public class PhotoDetailsModel {
    String ImageURL="";
    int id,Image;
    String imageName="";
    int albumId;
    int createdBy;
    String createdOn="";
    int modifiedBy;
    String modifiedOn="";
    boolean isActive;


    public PhotoDetailsModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                imageName = resultJson.isNull("ImageName") ? "" : resultJson.getString("ImageName");
                albumId = resultJson.isNull("AlbumId") ? 0 : resultJson.getInt("AlbumId");
                ImageURL = resultJson.isNull("ImageURL") ? "" : resultJson.getString("ImageURL");
                createdBy = resultJson.isNull("CreatedBy") ? 0 : resultJson.getInt("CreatedBy");
                createdOn = resultJson.isNull("CreatedOn") ? "" : resultJson.getString("CreatedOn");
                modifiedBy = resultJson.isNull("ModifiedBy") ? 0 : resultJson.getInt("ModifiedBy");
                modifiedOn = resultJson.isNull("ModifiedOn") ? "" : resultJson.getString("ModifiedOn");
                isActive = resultJson.isNull("IsActive") ? false : resultJson.getBoolean("IsActive");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public PhotoDetailsModel(String imageName,String ImageURL,int id,int createdId ){
        this.imageName=imageName;
        this.ImageURL=ImageURL;
        this.id=id;
        this.createdBy=createdId;
    }
    public PhotoDetailsModel(String imageName,String ImageURL){
        this.imageName=imageName;
        this.ImageURL=ImageURL;
    }
    public PhotoDetailsModel(String imageName,int Image){
        this.imageName=imageName;
        this.Image=Image;
    }
    public PhotoDetailsModel() {
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
