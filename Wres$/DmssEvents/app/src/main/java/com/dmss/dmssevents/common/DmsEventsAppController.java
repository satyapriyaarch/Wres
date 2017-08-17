package com.dmss.dmssevents.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.dmss.dmssevents.backendservice.WebService;
import com.dmss.dmssevents.models.AlbumsModel;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.EventsDetailsModel;
import com.dmss.dmssevents.models.PhotoDetailsModel;
import com.dmss.dmssevents.models.UserProfileModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * DmsEventsAppController.java -
 *
 * @author Jaya Krishna, sandeepkumar
 * @version 1.0
 * @see MultiDexApplication
 * @since 12-03-2017.
 */
public class DmsEventsAppController extends MultiDexApplication {
    DmsEventsAppController controller;
    Validation validation;
    String emailID = "";
    WebService webService;
    AlbumsModel selectedAlbum;
    PhotoDetailsModel photoDetailsModel;
    EventRatingModel eventRatingModel;
    boolean makeServiceCall = false;
    UserProfileModel userProfileModel;
    EventsDetailsModel selectedEvent;
    String remoteControllerKey="";

    FirebaseStorage storage;
    StorageReference storageRef;


    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        initalizeAllElements();
    }

    private void initalizeAllElements() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(ConstantKeys.fireBaseLinkKey);
        webService = new WebService(getContext());
        validation = new Validation(getContext());
        userProfileModel = DmsSharedPreferences.getUserDetails(getContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public void setStorageRef(StorageReference storageRef) {
        this.storageRef = storageRef;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public EventsDetailsModel getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(EventsDetailsModel selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public DmsEventsAppController getInstance() {
        return controller;
    }

    public DmsEventsAppController getController() {
        return controller;
    }

    public void setController(DmsEventsAppController controller) {
        this.controller = controller;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public AlbumsModel getSelectedAlbum() {
        return selectedAlbum;
    }

    public void setSelectedAlbum(AlbumsModel selectedAlbum) {
        this.selectedAlbum = selectedAlbum;
    }

    public PhotoDetailsModel getPhotoDetailsModel() {
        return photoDetailsModel;
    }

    public void setPhotoDetailsModel(PhotoDetailsModel photoDetailsModel) {
        this.photoDetailsModel = photoDetailsModel;
    }

    public EventRatingModel getEventRatingModel() {
        return eventRatingModel;
    }

    public void setEventRatingModel(EventRatingModel eventRatingModel) {
        this.eventRatingModel = eventRatingModel;
    }

    public boolean isMakeServiceCall() {
        return makeServiceCall;
    }

    public void setMakeServiceCall(boolean makeServiceCall) {
        this.makeServiceCall = makeServiceCall;
    }

    public UserProfileModel getUserProfileModel() {
        return userProfileModel;
    }

    public void setUserProfileModel(UserProfileModel userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    public String getRemoteControllerKey() {
        return remoteControllerKey;
    }

    public void setRemoteControllerKey(String remoteControllerKey) {
        this.remoteControllerKey = remoteControllerKey;
    }
}
