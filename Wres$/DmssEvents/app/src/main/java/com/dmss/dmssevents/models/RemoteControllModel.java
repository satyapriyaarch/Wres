package com.dmss.dmssevents.models;

/**
 * Created by sandeep.kumar on 13-04-2017.
 */
public class RemoteControllModel {
    String eventListItemDescription;
    int id;
    int selectedPosition;

    public RemoteControllModel(String eventListItemDescription, int id) {
        this.eventListItemDescription = eventListItemDescription;
        this.id = id;
        this.selectedPosition = selectedPosition;
    }

    public String getEventListItemDescription() {
        return eventListItemDescription;
    }

    public void setEventListItemDescription(String eventListItemDescription) {
        this.eventListItemDescription = eventListItemDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
