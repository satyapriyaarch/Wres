package com.dmss.dmssevents;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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

import com.dmss.dmssevents.adapters.AwardsAdapter;
import com.dmss.dmssevents.adapters.RatingAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.interfaces.AdapterCallForRating;
import com.dmss.dmssevents.interfaces.SubAdapterCallBackInterface;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.AwardsModel;
import com.dmss.dmssevents.models.EventRatingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 05-04-2017.
 */
public class AwardsActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, AdapterCallForRating, AdapterCallBack, SubAdapterCallBackInterface {
    ListView awardsListView;
    android.support.v7.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<AwardsModel> awardsModelArrayList = new ArrayList<AwardsModel>();
    AwardsAdapter awardsAdapter;
    LinearLayout emptyElement, awardDescription;
    TextView retryTextView;
    String profilePic = "";
    RelativeLayout awardOverViewLayout;
    ImageView closeImageView, awardImageView;
    boolean largeImageDisplayed = false;

    // private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);
        actionBarSettings();
        initializeUIElements();
       /* swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callWebApiForEventAwardDetails();
                                    }
                                }
        );*/

    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(AwardsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        awardsListView = (ListView) findViewById(R.id.ratingListView);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        awardOverViewLayout = (RelativeLayout) findViewById(R.id.awardOverViewLayout);
        closeImageView = (ImageView) findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        awardImageView = (ImageView) findViewById(R.id.awardImageView);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //swipeRefreshLayout.setOnRefreshListener(this);
        awardDescription = (LinearLayout) findViewById(R.id.awardDescription);
        /*awardDescription.setVisibility(View.VISIBLE);
        awardsListView.setVisibility(View.GONE);*/
        callWebApiForEventAwardDetails();


    }

    private void callWebApiForEventAwardDetails() {
        if (Utils.isNetworkAvailable(AwardsActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            String url= ConstantKeys.awards;
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForEventAwardDetails();
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeImageView:
                awardOverViewLayout.setVisibility(View.GONE);
                largeImageDisplayed = false;
                break;
        }
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
        imgReferesh.setVisibility(View.GONE);
        actionBarHeadingTextView.setText("Events Awards");
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
                    awardsModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AwardsModel awardsModel = new AwardsModel(jsonArray.getJSONObject(i));
                        awardsModel.setAwardImage(saveProfilePicUrl(awardsModel.getId()));
                        awardsModelArrayList.add(awardsModel);
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (awardsAdapter == null) {
                                    awardsAdapter = new AwardsAdapter(AwardsActivity.this, awardsModelArrayList);
                                    awardsListView.setAdapter(awardsAdapter);
                                    awardsListView.setEmptyView(findViewById(R.id.emptyElement));
                                } else {
                                    awardsAdapter.notifyDataSetChanged();
                                }
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        }));

                    }
                    //  }
                } else {
                    Utils.showToast(AwardsActivity.this, "Server Error");
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
            Utils.showToast(AwardsActivity.this, error);
        } else {
            Utils.showToast(AwardsActivity.this, "Network Error");
        }
        progressDialog.cancel();
        // swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void adapterClickedPosition(int value, int eventID) {

    }

    /*@Override
    public void onRefresh() {
        callWebApiForEventAwardDetails();
    }*/

    public String saveProfilePicUrl(int id) {
        profilePic = "";
        final String imageName = ConstantKeys.fBAwardsAlbumName+"/" + id + "_AwardName.jpg";
        StorageReference childRef = controller.getStorageRef().child(imageName);
        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                profilePic = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                profilePic = "";
            }
        });
        return profilePic;
    }

    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {

        String url = awardsModelArrayList.get(position).getAwardImage();
        loadLargeImage(url);

    }

    @Override
    public void onBackPressed() {

        if (largeImageDisplayed) {
            awardOverViewLayout.setVisibility(View.GONE);
            largeImageDisplayed = false;
        } else {
            finish();
        }
    }

    @Override
    public void SubAdapterCallBack(String url) {
        loadLargeImage(url);
    }

    public void loadLargeImage(String url) {
        if (url != null && url.length() > 0) {
            awardOverViewLayout.setVisibility(View.VISIBLE);
            awardImageView.bringToFront();
            closeImageView.bringToFront();
            largeImageDisplayed = true;
            Picasso.with(AwardsActivity.this).load(url).into(awardImageView);
        } else {
            Utils.showToast(AwardsActivity.this, "No Image to display");
        }
    }

}
