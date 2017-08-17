package com.dmss.dmssevents;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.multidex.MultiDexApplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.adapters.AlbumGridAdapter;
import com.dmss.dmssevents.adapters.AlbumsListAdapter;
import com.dmss.dmssevents.adapters.EventsAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.AlbumDetailsModel;
import com.dmss.dmssevents.models.AlbumsModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * EventAlbumsList.java -
 *
 * @author Jaya Krishna, sandeepkumar
 * @version 1.0
 * @see DmsEventsBaseActivity
 * @since 12-03-2017.
 */
public class EventAlbumsList extends DmsEventsBaseActivity implements WebServiceResponseCallBack {

    GridView albumsGridView;
    ArrayList<AlbumsModel> albumDetailsModels = new ArrayList<AlbumsModel>();
    AlbumGridAdapter albumGridAdapter;
    ActionBar actionBar;
    Display display;
    DisplayMetrics metrics;
    Dialog createAlbumDialog;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    static int AlbumsListApiCall = 1, AddAlbumApiCall = 2, DeleteAlbumApiCAll = 3;
    int apiCall;
    int viewDimensions, requestCode = 222;
    Context context;
    int selectedAlbumIdToDelete;
    String albumName;
    boolean resumeCall = true;
    LinearLayout emptyElement;
    TextView retryTextView,noAlbumsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_albums_list);
        resumeCall = false;
        getDisplayDimensions();
        actionBarSettings();
        initializeUIElements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(resumeCall && controller.isMakeServiceCall()){
            progressDialog.show();
            callWebApi(true);
            controller.setMakeServiceCall(false);
            resumeCall = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumeCall = true;
    }

    @Override
    public void initializeUIElements() {
        context = EventAlbumsList.this;
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(EventAlbumsList.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        albumsGridView = (GridView) findViewById(R.id.albumsGridView);
        emptyElement=(LinearLayout)findViewById(R.id.emptyElement);
        retryTextView=(TextView)findViewById(R.id.retryTextView);
        noAlbumsTextView=(TextView)findViewById(R.id.noAlbumsTextView);
        albumsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.setSelectedAlbum(albumDetailsModels.get(position));
                /*if (albumDetailsModels.get(position).getPhotosCount() > 0) {*/
                Intent intent = new Intent(EventAlbumsList.this, GalleryActivity.class);
                startActivityForResult(intent, requestCode);
                /*} else {
                    Toast.makeText(EventAlbumsList.this, "No Images to display", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        /*albumsGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumDetailsModels.get(position).getCreatedBy() == DmsSharedPreferences.getUserDetails(EventAlbumsList.this).getId()) {
                    selectedAlbumIdToDelete = albumDetailsModels.get(position).getId();
                    albumName = albumDetailsModels.get(position).getAlbumName();
                    DialogToDeleteAlbum(albumName);

                } else {
                    Utils.showToast(EventAlbumsList.this, "This album is created by someone else, you cannot delete this...!");
                }
                return false;
            }
        });*/
        viewDimensions = ((metrics.widthPixels) * 50) / 100;
        callWebApi(true);
    }

    private void DialogToDeleteAlbum(String albumName) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.deletealbum_dialog);
        LinearLayout linearLayoutDialog = (LinearLayout) dialog.findViewById(R.id.linearLayoutDialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout.LayoutParams allOptionsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewDimensions);
        linearLayoutDialog.setLayoutParams(allOptionsParams);
        TextView textViewDescription = (TextView) dialog.findViewById(R.id.textViewDescription);
        TextView textViewHeading = (TextView) dialog.findViewById(R.id.textViewHeading);
        TextView textViewSubmit = (TextView) dialog.findViewById(R.id.textViewSubmit);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.textViewCancel);
        textViewHeading.setText(albumName);
        textViewDescription.setText("Are you sure you want to delete this album?");

        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                callWebApi(false);
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * this method will be called when we want to get dimension of mobile screens
     *
     * @return void.
     */
    public void getDisplayDimensions() {
        WindowManager wm = (WindowManager) EventAlbumsList.this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);
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
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        TextView actionBarHeadingTextView = (TextView) view.findViewById(R.id.actionBarHeadingTextView);
        actionBarHeadingTextView.setText("Gallery");
        ImageView imgCreateFolder = (ImageView) view.findViewById(R.id.imgCreateFolder);
        imgCreateFolder.setVisibility(View.VISIBLE);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCreateAlbumDialog();
            }
        });
        actionBar.setCustomView(view, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    }

    private void callWebApi(boolean add) {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(EventAlbumsList.this)) {
            if (add) {
                apiCall = AlbumsListApiCall;
                progressDialog.show();
                albumsGridView.setVisibility(View.VISIBLE);
                emptyElement.setVisibility(View.GONE);
                controller.getWebService().getData(ConstantKeys.getAlbumsUrl + "1", this);
            } else {
                apiCall = DeleteAlbumApiCAll;
                progressDialog.show();
                albumsGridView.setVisibility(View.VISIBLE);
                emptyElement.setVisibility(View.GONE);
                controller.getWebService().deleteData(ConstantKeys.deleteAlbumUrl + Integer.toString(selectedAlbumIdToDelete), this);
            }

        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            albumsGridView.setVisibility(View.GONE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApi(true);
                }
            });
        }
    }

    public void addPhotosToAlbum(int position) {

    }

    public void staticData() {

    }

    public void displayCreateAlbumDialog() {
        createAlbumDialog = new Dialog(EventAlbumsList.this);

        createAlbumDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createAlbumDialog.setContentView(R.layout.create_album_dialog);

        final EditText albumNameEditText = (EditText) createAlbumDialog.findViewById(R.id.albumNameEditText);

        Button createAlbumButton = (Button) createAlbumDialog.findViewById(R.id.createAlbumButton);

        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumName = albumNameEditText.getText().toString().trim();
                createAlbum(albumName);
                createAlbumDialog.dismiss();

            }
        });
        albumNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String albumName = albumNameEditText.getText().toString().trim();
                    createAlbum(albumName);
                    createAlbumDialog.dismiss();
                }
                return false;
            }
        });


        createAlbumDialog.show();
    }

    public void createAlbum(String albumName) {
        if (!controller.getValidation().isNotNull(albumName)) {
            Toast.makeText(EventAlbumsList.this,
                    "album name should not be empty", Toast.LENGTH_LONG).show();
        } else {
            /**
             * Checking whether the network is available or not
             */
            progressDialog.show();
            if (Utils.isNetworkAvailable(EventAlbumsList.this)) {
                apiCall = AddAlbumApiCall;
                controller.getWebService().postData(ConstantKeys.createAlbumUrl, getCreateAlbumJsonData(albumName), this);
            } else {
                progressDialog.cancel();
            }
        }
    }

    public String getCreateAlbumJsonData(String albumName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AlbumName", albumName);
            jsonObject.put("EventId", controller.getSelectedEvent().getId());
            jsonObject.put("CreatedBy", DmsSharedPreferences.getUserDetails(EventAlbumsList.this).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            if (apiCall == AlbumsListApiCall) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    //Utils.showToast(EventAlbumsList.this, jsonObject.getString(ConstantKeys.messageKey));
                    if (status) {
                        if (!jsonObject.isNull(ConstantKeys.resultKey)) {
                            albumDetailsModels.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                AlbumsModel albumsModel = new AlbumsModel(jsonObject1);
                                albumDetailsModels.add(albumsModel);

                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    albumGridAdapter = new AlbumGridAdapter(EventAlbumsList.this, albumDetailsModels, viewDimensions);
                                    albumsGridView.setAdapter(albumGridAdapter);
                                    checkGridElements();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            } else if (apiCall == AddAlbumApiCall) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    Utils.showToast(EventAlbumsList.this, jsonObject.getString(ConstantKeys.messageKey));
                    if (status) {
                        apiCall = AlbumsListApiCall;
                        callWebApi(true);
                    } else {
                        progressDialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.cancel();
                }
            } else if (apiCall == DeleteAlbumApiCAll) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    if(status==true) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //albumGridAdapter.notifyDataSetChanged();
                                callWebApi(true);
                            }
                        });

                        Utils.showToast(EventAlbumsList.this, jsonObject.getString(ConstantKeys.messageKey));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            }

        }
    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(EventAlbumsList.this, error);
        } else {
            Utils.showToast(EventAlbumsList.this, "Network Error");
        }
        progressDialog.cancel();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (createAlbumDialog.isShowing()) {
            createAlbumDialog.cancel();
        } else {
            finish();
        }
    }*/


    public void checkGridElements(){
        if(albumDetailsModels.size()>0){
            albumsGridView.setVisibility(View.VISIBLE);
            noAlbumsTextView.setVisibility(View.GONE);
        }else{
            noAlbumsTextView.setVisibility(View.VISIBLE);
            albumsGridView.setVisibility(View.GONE);
        }
    }
}
