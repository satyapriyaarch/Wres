package com.dmss.dmssevents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.adapters.EventsAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.EventsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * EventsList.java -
 *
 * @author sandeepkumar
 * @version 1.0
 * @see DmsEventsBaseActivity
 * @since 12-03-2017.
 */
public class EventsList extends DmsEventsBaseActivity implements WebServiceResponseCallBack,SwipeRefreshLayout.OnRefreshListener {
    ListView albumsListView;
    ArrayList<EventsDetailsModel> arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
    android.support.v7.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout emptyElement;
    TextView retryTextView;
    EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);
        actionBarSettings();
        initializeUIElements();

    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(EventsList.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        emptyElement=(LinearLayout)findViewById(R.id.emptyElement);
        retryTextView=(TextView)findViewById(R.id.retryTextView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        callWebApiForEventDetails();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callWebApiForEventDetails();
                                    }
                                }
        );

    }

    private void callWebApiForEventDetails() {
        if (Utils.isNetworkAvailable(EventsList.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            albumsListView.setVisibility(View.VISIBLE);
            String url=ConstantKeys.eventList;
            controller.getWebService().getData(url, this);
        }else{
            swipeRefreshLayout.setRefreshing(false);
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForEventDetails();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(view, new android.support.v7.app.ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM | android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME);
        TextView actionBarHeadingTextView = (TextView) view.findViewById(R.id.actionBarHeadingTextView);
        ImageView imageViewLogout = (ImageView) view.findViewById(R.id.imageViewLogout);
        imageViewLogout.setVisibility(View.VISIBLE);

        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setVisibility(View.VISIBLE);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.GONE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Dmss Events");
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialog();

            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventsList.this, ProfileActivity.class);
                startActivity(i);
            }
        });

    }

    private void LogOutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EventsList.this);
        dialog.setCancelable(false);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout ?" );
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DmsSharedPreferences.saveUserLoggedInStatus(EventsList.this, false);
                Intent i = new Intent(EventsList.this, MailValidationActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(EventsList.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                if (status) {
                    arrayListDetailsModels = null;
                    arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        EventsDetailsModel eventsDetailsModel = new EventsDetailsModel(jsonArray.getJSONObject(i));
                        arrayListDetailsModels.add(eventsDetailsModel);
                    }
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            eventsAdapter = null;
                            eventsAdapter = new EventsAdapter(EventsList.this, arrayListDetailsModels,controller);
                            albumsListView.setAdapter(eventsAdapter);
                            //albumsListView.setEmptyView(findViewById(R.id.emptyElement));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }));
                    //  }
                } else {
                    Utils.showToast(EventsList.this, "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();

    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(EventsList.this, error);
        } else {
            Utils.showToast(EventsList.this, "Network Error");
        }
        progressDialog.cancel();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        callWebApiForEventDetails();
    }
}
