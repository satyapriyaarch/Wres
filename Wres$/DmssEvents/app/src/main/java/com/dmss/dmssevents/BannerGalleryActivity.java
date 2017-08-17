package com.dmss.dmssevents;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmss.dmssevents.adapters.BannerGalleryViewPagerAdapter;
import com.dmss.dmssevents.adapters.GalleryRecycleViewAdapter;
import com.dmss.dmssevents.adapters.GalleryViewPagerAdapter;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.PhotoDetailsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class BannerGalleryActivity extends DmsEventsBaseActivity implements AdapterCallBack, WebServiceResponseCallBack {
    public static int bannerPosition;
    RecyclerView recyclerView;
    ViewPager imageViewPager;
    DmsEventsAppController controller;
    GalleryRecycleViewAdapter galleryRecycleViewAdapter;
    BannerGalleryViewPagerAdapter galleryViewPagerAdapter;
    public static final String TAG = "BannerGalleryActivity";
    public static final String EXTRA_NAME = "images";
    android.support.v7.app.ActionBar actionBar;
    Context thisContext;
    int selectedPosition = 0;
    ProgressDialog progressDialog;
    Context context;
    PhotoDetailsModel model;
    public static ArrayList<PhotoDetailsModel> photoDetailsModels = new ArrayList<PhotoDetailsModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_gallery);
        actionBarSettings();
        initializeUIElements();
    }

    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {
        imageViewPager.setCurrentItem(position, false);
        selectedPosition = position;
    }

    @Override
    public void initializeUIElements() {
       /* progressDialog = new ProgressDialog(BannerGalleryActivity.this);
        progressDialog.setMessage("Loading...,please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);*/
        selectedPosition = bannerPosition;
        context = BannerGalleryActivity.this;
        controller = (DmsEventsAppController) getApplicationContext();
        thisContext = BannerGalleryActivity.this;
        //model=controller.getPhotoDetailsModel();
        //photoDetailsModels.add(model);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewBanner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext, LinearLayoutManager.HORIZONTAL, false));
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPagerBanner);
        galleryViewPagerAdapter = new BannerGalleryViewPagerAdapter(photoDetailsModels, thisContext);
        galleryRecycleViewAdapter = new GalleryRecycleViewAdapter(photoDetailsModels, thisContext, selectedPosition, DmsSharedPreferences.getUserDetails(BannerGalleryActivity.this).getId());
        recyclerView.setAdapter(galleryRecycleViewAdapter);
        imageViewPager.setAdapter(galleryViewPagerAdapter);
        imageViewPager.setCurrentItem(selectedPosition, false);
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                galleryRecycleViewAdapter.setSelectedPosition(position);
                recyclerView.smoothScrollToPosition(position);
                selectedPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        ImageView imgCreateFolder = (ImageView) view.findViewById(R.id.imgCreateFolder);
        imgCreateFolder.setVisibility(View.GONE);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        actionBarHeadingTextView.setText("Events Gallery");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onServiceCallSuccess(String result) {

    }

    @Override
    public void onServiceCallFail(String error) {

    }
}
