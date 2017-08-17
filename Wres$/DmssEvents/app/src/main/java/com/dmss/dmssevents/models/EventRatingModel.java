package com.dmss.dmssevents.models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 31-03-2017.
 */
public class EventRatingModel {
    int Id, TotalVote, RateAvarage;
    String TPEventName = "", TPEventDescription = "";
    boolean IsActive, IsRated;
    int enabledDisabldValue=0;
    ArrayList<ParticipantsModel> arrayParticipantsModels = new ArrayList<ParticipantsModel>();


    public EventRatingModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                Id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                TotalVote = resultJson.isNull("TotalVote") ? 0 : resultJson.getInt("TotalVote");
                RateAvarage = resultJson.isNull("RateAvarage") ? 0 : resultJson.getInt("RateAvarage");
                TPEventName = resultJson.isNull("TPEventName") ? "" : resultJson.getString("TPEventName");
                TPEventDescription = resultJson.isNull("TPEventDescription") ? "" : resultJson.getString("TPEventDescription");
                IsActive = resultJson.isNull("IsActive") ? false : resultJson.getBoolean("IsActive");
                IsRated = resultJson.isNull("IsRated") ? false : resultJson.getBoolean("IsRated");
                if (!resultJson.isNull("Participants")) {
                    JSONArray participantsListJson = resultJson.getJSONArray("Participants");
                    if (participantsListJson.length() > 0) {
                        for (int i = 0; i < participantsListJson.length(); i++) {
                            ParticipantsModel pariticipantsDetailsModel = new ParticipantsModel(participantsListJson.getJSONObject(i));
                            arrayParticipantsModels.add(pariticipantsDetailsModel);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getEnabledDisabldValue() {
        return enabledDisabldValue;
    }

    public void setEnabledDisabldValue(int enabledDisabldValue) {
        this.enabledDisabldValue = enabledDisabldValue;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTotalVote() {
        return TotalVote;
    }

    public void setTotalVote(int totalVote) {
        TotalVote = totalVote;
    }

    public int getRateAvarage() {
        return RateAvarage;
    }

    public void setRateAvarage(int rateAvarage) {
        RateAvarage = rateAvarage;
    }

    public String getTPEventName() {
        return TPEventName;
    }

    public void setTPEventName(String TPEventName) {
        this.TPEventName = TPEventName;
    }

    public String getTPEventDescription() {
        return TPEventDescription;
    }

    public void setTPEventDescription(String TPEventDescription) {
        this.TPEventDescription = TPEventDescription;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public boolean isRated() {
        return IsRated;
    }

    public void setIsRated(boolean isRated) {
        IsRated = isRated;
    }

    public ArrayList<ParticipantsModel> getArrayParticipantsModels() {
        return arrayParticipantsModels;
    }

    public void setArrayParticipantsModels(ArrayList<ParticipantsModel> arrayParticipantsModels) {
        this.arrayParticipantsModels = arrayParticipantsModels;
    }
}
