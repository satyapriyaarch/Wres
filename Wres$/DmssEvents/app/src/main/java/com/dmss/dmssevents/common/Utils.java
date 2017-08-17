package com.dmss.dmssevents.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.dmss.dmssevents.R;

/**
 * Created by jaya.krishna on 20-Mar-17.
 */
public class Utils {
    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            showToast(act, "Internet Unavailable");

            return false;
        }
    }


    public static void showToast(final Activity act, final String Message) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, Message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
