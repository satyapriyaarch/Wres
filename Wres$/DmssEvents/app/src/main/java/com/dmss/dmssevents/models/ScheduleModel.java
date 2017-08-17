package com.dmss.dmssevents.models;

import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 03-04-2017.
 */
public class ScheduleModel {
int Id;
    String StartDateTime="",EndDateTime="",ScheduleItem="",Description="",ColorCode="";
    boolean IsActive;

    public ScheduleModel(JSONObject resultJson){

        if(resultJson!=null){
            try{
                Id=resultJson.isNull("Id")?0:resultJson.getInt("Id");
                StartDateTime=resultJson.isNull("StartDateTime")?"":resultJson.getString("StartDateTime");
                EndDateTime=resultJson.isNull("EndDateTime")?"":resultJson.getString("EndDateTime");
                ScheduleItem=resultJson.isNull("ScheduleItem")?"":resultJson.getString("ScheduleItem");
                Description=resultJson.isNull("Description")?"":resultJson.getString("Description");
                ColorCode=resultJson.isNull("ColorCode")?"":resultJson.getString("ColorCode");
                IsActive=resultJson.isNull("IsActive")?false:resultJson.getBoolean("IsActive");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public String getScheduleItem() {
        return ScheduleItem;
    }

    public void setScheduleItem(String scheduleItem) {
        ScheduleItem = scheduleItem;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }
}
