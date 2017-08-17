package com.dmss.dmssevents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.dmss.dmssevents.adapters.RatingAdapter;
import com.dmss.dmssevents.adapters.RemoteControllerListAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.RemoteControllerForEnableDisable;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.EventsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.kumar on 13-04-2017.
 */
public class RemoteControlActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, RemoteControllerForEnableDisable {
    android.support.v7.app.ActionBar actionBar;
    TextView textViewKey, textViewSet, retryTextView;
    ListView controllerListView;
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    LinearLayout emptyElement, linearLayoutEntireList;
    ArrayList<EventRatingModel> eventRatingModelArrayList = new ArrayList<EventRatingModel>();
    RemoteControllerListAdapter remoteControllerListAdapter;
    int apiCall = 0;
    int selectedPosition;
    boolean keyCheckStatus = false;
    String[] ratingListNames = {"Opening Act By Chhaya", "Dance Performance - 3G", "Act it Out! - \\\"Sparsh The Touch\\",
            "Dance Performance - Shivaji R", "DJ 6-1", "Sprint Boys", "Dance Performance - Amit Malaviya",
            "Dance Performance - Akshay Kumar", "D J 6-2", "Dance Performance - Megha Sengupta", "Best Foot Forward - Group\\r\\n", "YLS"};
    int[] eventID = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        actionBarSettings();
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(RemoteControlActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        textViewKey = (TextView) findViewById(R.id.textViewKey);
        textViewSet = (TextView) findViewById(R.id.textViewSet);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        linearLayoutEntireList = (LinearLayout) findViewById(R.id.linearLayoutEntireList);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        controllerListView = (ListView) findViewById(R.id.controllerListView);
        textViewKey.setText("b9jQq35n2H");
        callWebApiForRemoteControllAccess();
        textViewSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewKey.getText().toString().equalsIgnoreCase("b9jQq35n2H")) {
                    keyCheckStatus = true;
                    controller.setRemoteControllerKey(textViewKey.getText().toString());
                    textViewKey.setEnabled(false);
                    textViewSet.setBackgroundResource(R.drawable.green_round_corner_bg);
                } else {
                    Toast.makeText(RemoteControlActivity.this, "Wrong Key", Toast.LENGTH_SHORT).show();
                    textViewKey.setEnabled(true);
                    textViewSet.setBackgroundResource(R.drawable.orange_round_corner_bg);
                }
            }
        });
       /* for (int i = 0; i < ratingListNames.length; i++) {
            if (remoteControllerListAdapter == null) {
                RemoteControllModel model = new RemoteControllModel(ratingListNames[i], eventID[i]);
                remoteControllModelsArray.add(model);
                remoteControllerListAdapter = new RemoteControllerListAdapter(RemoteControlActivity.this, remoteControllModelsArray);

                controllerListView.setAdapter(remoteControllerListAdapter);

            } else {
                remoteControllerListAdapter.notifyDataSetChanged();
            }
        }*/
    }

    private void callWebApiForRemoteControllAccess() {
        if (Utils.isNetworkAvailable(RemoteControlActivity.this)) {
            progressDialog.show();
            apiCall = 1;
            emptyElement.setVisibility(View.GONE);
            linearLayoutEntireList.setVisibility(View.VISIBLE);
            String url = ConstantKeys.getPerformanceForRemoteControll;
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForRemoteControllAccess();
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
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialog();
            }
        });

        ImageView imgReferesh = (ImageView) view.findViewById(R.id.imgReferesh);
        imgReferesh.setVisibility(View.VISIBLE);
        imgReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebApiForRemoteControllAccess();
            }
        });
        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setVisibility(View.GONE);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Remote Control");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LogOutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RemoteControlActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout ?" );
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DmsSharedPreferences.saveUserLoggedInStatus(RemoteControlActivity.this, false);
                Intent i = new Intent(RemoteControlActivity.this, MailValidationActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(RemoteControlActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
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
                final boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
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
                                if (remoteControllerListAdapter == null) {
                                    remoteControllerListAdapter = new RemoteControllerListAdapter(RemoteControlActivity.this, eventRatingModelArrayList);
                                    controllerListView.setAdapter(remoteControllerListAdapter);


                                } else {
                                    remoteControllerListAdapter.notifyDataSetChanged();
                                }
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        }));

                    } else
                    if (apiCall == 2) {

                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        final boolean resultValue = jsonObject.getBoolean(ConstantKeys.resultKey);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (resultValue == true) {
                                    eventRatingModelArrayList.get(selectedPosition).setEnabledDisabldValue(1);
                                    remoteControllerListAdapter.notifyDataSetChanged();
                                    callWebApiForRemoteControllAccess();
                                    Toast.makeText(RemoteControlActivity.this, message, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    } else if (apiCall == 3) {
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        final boolean resultValue = jsonObject.getBoolean(ConstantKeys.resultKey);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (resultValue == true) {
                                    //eventRatingModelArrayList.get(selectedPosition).setEnabledDisabldValue(2);
                                    remoteControllerListAdapter.notifyDataSetChanged();
                                    Toast.makeText(RemoteControlActivity.this, message, Toast.LENGTH_SHORT).show();
                                    /*if (remoteControllerListAdapter == null) {
                                        remoteControllerListAdapter = new RemoteControllerListAdapter(RemoteControlActivity.this, apiCall,status);

                                    } else {
                                        remoteControllerListAdapter.notifyDataSetChanged();
                                    }*/
                                }
                            }
                        });

                    }
                    //  }
                } else {
                    Utils.showToast(RemoteControlActivity.this, "Server Error");
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
            Utils.showToast(RemoteControlActivity.this, error);
        } else {
            Utils.showToast(RemoteControlActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }


    @Override
    public void adapterClickedPosition(int clickID, int tpEventID, int position) {
        selectedPosition = position;
        if (clickID == 1 && keyCheckStatus == true) {
            String key = textViewKey.getText().toString();
            if (controller.getValidation().isNotNull(key)) {
                callWebServiceForEnableFunctionality(tpEventID);
            } else {
                Toast.makeText(RemoteControlActivity.this, "Please Enter key and verify", Toast.LENGTH_SHORT).show();
            }

        } else if (clickID == 2) {
            String key = textViewKey.getText().toString();
            if (controller.getValidation().isNotNull(key)) {
                callWebServiceForDisableFunctionality(tpEventID);
            } else {
                Toast.makeText(RemoteControlActivity.this, "Enter key before enable", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(RemoteControlActivity.this, "Set key and verify before enable this functionality", Toast.LENGTH_SHORT).show();
        }
    }

    private void callWebServiceForEnableFunctionality(int tpEventID) {
        if (Utils.isNetworkAvailable(RemoteControlActivity.this)) {
            progressDialog.show();
            apiCall = 2;
            emptyElement.setVisibility(View.GONE);
            linearLayoutEntireList.setVisibility(View.VISIBLE);
            String url = ConstantKeys.enableEvents + tpEventID + "/" + controller.getRemoteControllerKey();
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForRemoteControllAccess();
                }
            });
        }
    }


    private void callWebServiceForDisableFunctionality(int tpEventID) {
        if (Utils.isNetworkAvailable(RemoteControlActivity.this)) {
            progressDialog.show();
            apiCall = 3;
            emptyElement.setVisibility(View.GONE);
            linearLayoutEntireList.setVisibility(View.VISIBLE);
            String url = ConstantKeys.disableEvents + tpEventID + "/" + controller.getRemoteControllerKey();
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForRemoteControllAccess();
                }
            });
        }
        Toast.makeText(RemoteControlActivity.this, "Disable Button Clicked", Toast.LENGTH_SHORT).show();
    }
}
