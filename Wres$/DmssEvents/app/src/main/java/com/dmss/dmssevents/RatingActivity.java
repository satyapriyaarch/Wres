package com.dmss.dmssevents;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.adapters.EventsAdapter;
import com.dmss.dmssevents.adapters.RatingAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.TouchImageView;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.interfaces.AdapterCallForRating;
import com.dmss.dmssevents.interfaces.SubAdapterCallBackInterface;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.EventsDetailsModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class RatingActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, AdapterCallForRating, SwipeRefreshLayout.OnRefreshListener, SubAdapterCallBackInterface {
    ListView ratingListView;
    android.support.v7.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<EventRatingModel> eventRatingModelArrayList = new ArrayList<EventRatingModel>();
    int apiCall = 0;
    RatingAdapter ratingAdapter;
    Timer timer = new Timer();
    //private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout emptyElement;
    TextView retryTextView,emptyTextView;
    RelativeLayout ratingOverViewLayout;
    ImageView closeRatingImageView;
    TouchImageView ratingImageView;
    boolean imageVisible =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        actionBarSettings();
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(RatingActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        ratingListView = (ListView) findViewById(R.id.ratingListView);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        ratingOverViewLayout = (RelativeLayout) findViewById(R.id.ratingOverViewLayout);
        closeRatingImageView = (ImageView) findViewById(R.id.closeRatingImageView);
        closeRatingImageView.setOnClickListener(this);
        ratingImageView = (TouchImageView) findViewById(R.id.ratingImageView);
        //ratingListView.setEmptyView(findViewById(R.id.emptyElement));
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //swipeRefreshLayout.setOnRefreshListener(this);
        callWebApiForEventRatingDetails();
        retryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebApiForEventRatingDetails();
            }
        });
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callWebApiForEventRatingDetails();
                                    }
                                }
        );*/


    }


    private void callWebApiForEventRatingDetails() {
        if (Utils.isNetworkAvailable(RatingActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            apiCall = 1;
            String url = ConstantKeys.getPerformances + DmsSharedPreferences.getUserDetails(RatingActivity.this).getId();
            controller.getWebService().getData(url, this);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            progressDialog.cancel();
            emptyTextView.setText("No Internet available..Please");
            retryTextView.setText("Retry");
            emptyElement.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        closeRatingImageView.setVisibility(View.GONE);
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
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        ImageView imgReferesh = (ImageView) view.findViewById(R.id.imgReferesh);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        imgReferesh.setVisibility(View.VISIBLE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Audience Poll");
        imgReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                callWebApiForEventRatingDetails();
            }
        });
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    if (apiCall == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        eventRatingModelArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EventRatingModel eventsRatingModel = new EventRatingModel(jsonArray.getJSONObject(i));
                            eventRatingModelArrayList.add(eventsRatingModel);
                            controller.setEventRatingModel(eventsRatingModel);
                        }
                        int size = eventRatingModelArrayList.size();
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (ratingAdapter == null) {
                                    ratingAdapter = new RatingAdapter(RatingActivity.this, eventRatingModelArrayList);
                                    ratingListView.setAdapter(ratingAdapter);


                                } else {
                                    ratingAdapter.notifyDataSetChanged();
                                }
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        }));

                    } else if (apiCall == 2) {
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RatingActivity.this, message, Toast.LENGTH_SHORT).show();
                                callWebApiForEventRatingDetails();
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                    //  }
                } else {
                    Utils.showToast(RatingActivity.this, "Server Error");
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
            Utils.showToast(RatingActivity.this, error);
        } else {
            Utils.showToast(RatingActivity.this, "Network Error");
        }
        progressDialog.cancel();
        //swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void adapterClickedPosition(int value, int id) {
        apiCall = 2;
        progressDialog.show();
        callWebApiForRating(value, id);

    }

    private void callWebApiForRating(int ratingValue, int id) {
        if (Utils.isNetworkAvailable(RatingActivity.this)) {
            String url = ConstantKeys.rating;
            controller.getWebService().postData(url, getJsonDataForRating(ratingValue, id), this);
        } else {
            progressDialog.cancel();
        }
    }

    private String getJsonDataForRating(int ratingValue, int id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RatedBy", DmsSharedPreferences.getUserDetails(RatingActivity.this).getId());
            jsonObject.put("TPEventId", id);
            jsonObject.put("Rate", ratingValue);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject.toString();
    }


    @Override
    public void onRefresh() {
        callWebApiForEventRatingDetails();
    }

    @Override
    public void SubAdapterCallBack(String url) {
        if (url != null && url.length() > 0) {
            ratingOverViewLayout.setVisibility(View.VISIBLE);
            ratingImageView.bringToFront();
            closeRatingImageView.bringToFront();
            imageVisible = true;
            Picasso.with(RatingActivity.this).load(url).into(ratingImageView);
        } else {
            Utils.showToast(RatingActivity.this, "No Image to display");
        }
    }

    @Override
    public void onBackPressed() {
        if (imageVisible) {
            ratingOverViewLayout.setVisibility(View.GONE);
            imageVisible = false;
        } else {
            finish();
        }
    }

    public void nodata(){
        emptyElement.setVisibility(View.VISIBLE);
        emptyTextView.setText("Please wait for completion of team performance");
        retryTextView.setText("Refresh to see Ratings");
    }
}
