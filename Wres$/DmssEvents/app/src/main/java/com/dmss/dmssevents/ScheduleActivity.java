package com.dmss.dmssevents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dmss.dmssevents.adapters.EventsAdapter;
import com.dmss.dmssevents.adapters.ScheduleAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.EventsDetailsModel;
import com.dmss.dmssevents.models.ScheduleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 28-03-2017.
 */
public class ScheduleActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack {
    ListView mScheduleListView;
    android.support.v7.app.ActionBar actionBar;
    String[] mTitle, mTime;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<ScheduleModel> scheduleModelArrayList=new ArrayList<ScheduleModel>();
    LinearLayout emptyElement;
    TextView retryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        actionBarSettings();
        initializeUIElements();
    }




    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(ScheduleActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        mScheduleListView = (ListView) findViewById(R.id.mScheduleListView);
        emptyElement=(LinearLayout)findViewById(R.id.emptyElement);
        retryTextView=(TextView)findViewById(R.id.retryTextView);
        callWebApiForScheduleList();
        mScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(ScheduleActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setCancelable(false);
                dialog.setContentView(R.layout.schedule_dialog);
                TextView textViewHeading = (TextView) dialog.findViewById(R.id.textViewHeading);
                TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
                textViewHeading.setText(scheduleModelArrayList.get(position).getDescription());
                String color = scheduleModelArrayList.get(position).getColorCode();
                textViewTitle.setBackgroundColor(Color.parseColor(color));

                dialog.show();
            }
        });


    }

    private void callWebApiForScheduleList() {
        if (Utils.isNetworkAvailable(ScheduleActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            String url= ConstantKeys.schedule;
            controller.getWebService().getData(url, this);
        }else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForScheduleList();
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
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Milestone-2017-Schedule");
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
                JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                if (status) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ScheduleModel scheduleModel=new ScheduleModel(jsonArray.getJSONObject(i));

                        scheduleModelArrayList.add(scheduleModel);
                    }
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                                mScheduleListView.setAdapter(new ScheduleAdapter(ScheduleActivity.this, scheduleModelArrayList));
                            //mScheduleListView.setEmptyView(findViewById(R.id.emptyElement));
                        }
                    }));


                    //  }
                } else {
                    Utils.showToast(ScheduleActivity.this, "Server Error");
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
            Utils.showToast(ScheduleActivity.this, error);
        } else {
            Utils.showToast(ScheduleActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }
}
