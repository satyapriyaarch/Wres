package com.dmss.dmssevents.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.models.UserProfileModel;
import com.google.gson.Gson;

/**
 * DmsSharedPreferences.java -
 *
 * @author Jaya Krishna
 * @version 1.0
 * @since 09-03-2017.
 */
public class DmsSharedPreferences {

    @SuppressWarnings("static-access")
    public static void saveUserDetails(Context context, UserProfileModel userProfileModel) {
        Gson gson = new Gson();
        String userProfileModelJson = gson.toJson(userProfileModel);
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.pref_userDetails), userProfileModelJson)
                .commit();
    }

    @SuppressWarnings("static-access")
    public static UserProfileModel getUserDetails(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        String vendorsDetailsJson = pref.getString(context.getString(R.string.pref_userDetails), null);
        Gson gson = new Gson();
        return gson.fromJson(vendorsDetailsJson, UserProfileModel.class);
    }
    @SuppressWarnings("static-access")
    public static void saveUserLoggedInStatus(Context context, boolean status) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putBoolean(context.getString(R.string.pref_userLoggedIn), status)
                .commit();
    }

    @SuppressWarnings("static-access")
    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        boolean status = pref.getBoolean(context.getString(R.string.pref_userLoggedIn), false);
        return status;
    }

    @SuppressWarnings("static-access")
    public static void saveLastLoggedInId(Context context, int id) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putInt(context.getString(R.string.pref_lastLoggedInId), id)
                .commit();
    }

    @SuppressWarnings("static-access")
    public static int getLastLoggedInId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        int id = pref.getInt(context.getString(R.string.pref_lastLoggedInId), 0);
        return id;
    }

    @SuppressWarnings("static-access")
    public static void saveProfilePicUrl(Context context, String url) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.pref_profilePicUrl), url)
                .commit();
    }

    @SuppressWarnings("static-access")
    public static String getProfilePicUrl(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        String url = pref.getString(context.getString(R.string.pref_profilePicUrl), "");
        return url;
    }

    @SuppressWarnings("static-access")
    public static void saveOwnerLoggedInStatus(Context context, boolean status) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putBoolean("isOwner", status)
                .commit();
    }

    @SuppressWarnings("static-access")
    public static boolean isOwnerLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        boolean status = pref.getBoolean("isOwner", false);
        return status;
    }


}
