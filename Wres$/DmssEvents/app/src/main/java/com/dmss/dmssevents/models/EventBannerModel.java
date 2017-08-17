package com.dmss.dmssevents.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class EventBannerModel {
    String title="",eventDate="";
    String CoverImageURL="";
    public EventBannerModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                //Id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                title = resultJson.isNull("title") ? "" : resultJson.getString("title");
                CoverImageURL = resultJson.isNull("url") ? "" : resultJson.getString("url");
                eventDate = resultJson.isNull("EventDate") ? "" : resultJson.getString("EventDate");

           /*     if (!resultJson.isNull("EventConverList")) {
                    JSONArray EventConverListJson = resultJson.getJSONArray("EventConverList");
                    for (int i = 0; i < EventConverListJson.length(); i++) {
                        String test = "", test2 = "";
                        test = EventConverListJson.getString(i);
                        test2 = test.replaceAll("\\s+", "%20");
                        EventConverListArray.add(test2);
                    }
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getCoverImageURL() {
        return CoverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        CoverImageURL = coverImageURL;
    }
}
