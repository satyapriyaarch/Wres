package com.dmss.dmssevents.models;

import com.dmss.dmssevents.EventsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jaya.krishna on 3/12/2017.
 */
public class EventsDetailsModel {

    String eventName = "", eventDate = "", eventCreatedDate = "", CoverImageURL = "", LaunchDate = "", Description = "", ModifiedBy = "";
    ArrayList<AlbumDetailsModel> albumDetailsArray = new ArrayList<AlbumDetailsModel>();
    int Id, CreatedBy, CreatedOn, ModifiedOn;
    ArrayList<String> EventConverListArray = new ArrayList<String>();
    boolean IsActive;
    String[] EventConverList = {""};

    public EventsDetailsModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                Id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                eventName = resultJson.isNull("EventName") ? "" : resultJson.getString("EventName");
                CoverImageURL = resultJson.isNull("CoverImageURL") ? "" : resultJson.getString("CoverImageURL");
                eventDate = resultJson.isNull("EventDate") ? "" : resultJson.getString("EventDate");
                LaunchDate = resultJson.isNull("LaunchDate") ? "" : resultJson.getString("LaunchDate");
                Description = resultJson.isNull("Description") ? "" : resultJson.getString("Description");
                CreatedBy = resultJson.isNull("CreatedBy") ? 0 : resultJson.getInt("CreatedBy");
                CreatedOn = resultJson.isNull("CreatedOn") ? 0 : resultJson.getInt("CreatedOn");
                ModifiedBy = resultJson.isNull("ModifiedBy") ? "" : resultJson.getString("ModifiedBy");
                ModifiedOn = resultJson.isNull("ModifiedOn") ? 0 : resultJson.getInt("ModifiedOn");
                IsActive = resultJson.isNull("IsActive") ? false : resultJson.getBoolean("IsActive");
                if (!resultJson.isNull("EventConverList")) {
                    JSONArray EventConverListJson = resultJson.getJSONArray("EventConverList");
                    for (int i = 0; i < EventConverListJson.length(); i++) {
                        String test = "", test2 = "";
                        test = EventConverListJson.getString(i);
                        test2 = test.replaceAll("\\s+", "%20");
                        EventConverListArray.add(test2);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCreatedDate() {
        return eventCreatedDate;
    }

    public void setEventCreatedDate(String eventCreatedDate) {
        this.eventCreatedDate = eventCreatedDate;
    }

    public ArrayList<AlbumDetailsModel> getAlbumDetailsArray() {
        return albumDetailsArray;
    }

    public void setAlbumDetailsArray(ArrayList<AlbumDetailsModel> albumDetailsArray) {
        this.albumDetailsArray = albumDetailsArray;
    }

    public String getCoverImageURL() {
        return CoverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        CoverImageURL = coverImageURL;
    }

    public String getLaunchDate() {
        return LaunchDate;
    }

    public void setLaunchDate(String launchDate) {
        LaunchDate = launchDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(int createdOn) {
        CreatedOn = createdOn;
    }

    public int getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(int modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public ArrayList<String> getEventConverListArray() {
        return EventConverListArray;
    }

    public void setEventConverListArray(ArrayList<String> eventConverListArray) {
        EventConverListArray = eventConverListArray;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public String[] getEventConverList() {
        return EventConverList;
    }

    public void setEventConverList(String[] eventConverList) {
        EventConverList = eventConverList;
    }
}
