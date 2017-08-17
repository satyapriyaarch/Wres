package com.dmss.dmssevents.common;

/**
 * Created by jaya.krishna on 20-Mar-17.
 */
public class ConstantKeys {

    public static String versionCode = "Version 1.1";

    public static String fireBaseLinkKey = "gs://dmssevents-606bf.appspot.com";
    public static String fBGalleryAlbumName = "DMSS-Events-Images";
    public static String fBProfilePicsAlbumName = "Profile_pics";
    public static String fBAwardsAlbumName = "Milestone2017_awards";




    public static String baseUrl = "http://www.dmss.co.in/dmssapp/api/"/*"http://192.168.100.92:1010/api/"*/;
    public static String loginUrl = baseUrl + "login/emplogin";
    public static String eventList = baseUrl+"events/eventlist";
    public static String getAlbumsUrl = baseUrl + "events/getAlbums/";
    public static String createAlbumUrl = baseUrl + "events/CreateAlbum";
    public static String deleteAlbumUrl = baseUrl+"events/deleteAlbum/";
    public static String deleteImageUrl = baseUrl+"events/deleteImage/";
    public static String addImagesUrl = baseUrl+"events/addImages";
    public static String getPerformances = baseUrl+"events/GetPerformer/";
    public static String rating = baseUrl+"events/rating";
    public static String schedule = baseUrl+"events/eventschedule";
    public static String checkUserUrl = baseUrl + "login/checkuser";
    public static String createPasswordUrl = baseUrl + "login/createpassword";
    public static String awards = baseUrl + "events/eventawards";
    public static String forgotPassword = baseUrl + "login/forgetPwd?EmailId=";
    public static String statusJsonKey = "Status";
    public static String isPasswordSet = "IsPasswordSet";
    public static String resultKey = "Result";
    public static String messageKey = "Message";
    public static String uploadedImagesIdKey = "ids";
    public static String enableEvents =baseUrl+"events/enableEvent/";
    public static String disableEvents =baseUrl+"events/disableEvent/";
    public static String getPerformanceForRemoteControll =baseUrl+"events/GetAllEventPerformer";
}
