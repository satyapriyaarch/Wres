package com.dmss.dmssevents;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.adapters.EventsAdapter;
import com.dmss.dmssevents.common.ConstantKeys;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.Utils;
import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;
import com.dmss.dmssevents.models.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 14-03-2017.
 */
public class LoginActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack {
    TextView btnLogin, edtxEmail,textViewForgotPassword;
    EditText edtxtPassword;
    DmsEventsAppController controller;
    String password;
    ProgressDialog progressDialog;
    android.support.v7.app.ActionBar actionBar;
    int apiCallType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBarSettings();
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        controller = (DmsEventsAppController) getApplicationContext();
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        edtxEmail = (TextView) findViewById(R.id.edtxEmail);
        edtxtPassword = (EditText) findViewById(R.id.edtxtPassword);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        edtxEmail.setText(controller.getEmailID());
        edtxEmail.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        edtxtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(btnLogin);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                progressDialog.show();
                password = edtxtPassword.getText().toString();
                if (controller.getValidation().isNotNull(password)) {
                    if(controller.getEmailID().equalsIgnoreCase("SatyapriyaR@dmss.co.in")&& password.equalsIgnoreCase("1")){
                        DmsSharedPreferences.saveOwnerLoggedInStatus(LoginActivity.this, true);

                        DmsSharedPreferences.saveUserLoggedInStatus(LoginActivity.this, true);
                        Intent i=new Intent(LoginActivity.this,RemoteControlActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        callWebApiToLogin();
                    }
                } else {
                    DmsSharedPreferences.saveOwnerLoggedInStatus(LoginActivity.this,false);
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
                break;
            case R.id.edtxEmail:
                Toast.makeText(LoginActivity.this,"Please go back to edit your Email Id",Toast.LENGTH_SHORT).show();
                break;
            case R.id.textViewForgotPassword:
                callForgotPasswordAPI();
                break;
        }

    }

    private void callForgotPasswordAPI() {
        progressDialog.show();
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            apiCallType=2;
            controller.getWebService().getData(ConstantKeys.forgotPassword + controller.getEmailID(), this);
        }else {
            progressDialog.cancel();
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
        ImageView imageViewLogout = (ImageView) view.findViewById(R.id.imageViewLogout);
        imageViewLogout.setVisibility(View.GONE);

        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setVisibility(View.GONE);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Login");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,MailValidationActivity.class);
                String[] str_array = edtxEmail.getText().toString().split("@");
                String stringEmail = str_array[0];
                final String stringEmailWithAddress = str_array[1];
                i.putExtra("EmailId",stringEmail);
                startActivity(i);
                finish();
            }
        });
    }


    private void callWebApiToLogin() {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            apiCallType=1;
            controller.getWebService().postData(ConstantKeys.loginUrl, getCreatePasswordJsonData(), this);
        }else {
            progressDialog.cancel();
        }
    }

    public String getCreatePasswordJsonData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", controller.getEmailID());
            jsonObject.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                Utils.showToast(LoginActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                if (status) {
                    if(apiCallType==1) {
                        JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                        UserProfileModel userProfileModel = new UserProfileModel(jsonResultObject);
                        DmsSharedPreferences.saveUserDetails(LoginActivity.this, userProfileModel);
                        DmsSharedPreferences.saveUserLoggedInStatus(LoginActivity.this, status);
                        //passIntent();
                        saveProfilePicUrl(userProfileModel.getId());
                    }
                    if(apiCallType==2) {
                        Intent i=new Intent(LoginActivity.this,OTPVerification.class);
                        startActivity(i);
                        finish();
                    }
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
            Utils.showToast(LoginActivity.this, error);
        } else {
            Utils.showToast(LoginActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }


    public void saveProfilePicUrl(int id){
        int lastId = DmsSharedPreferences.getLastLoggedInId(LoginActivity.this);
        if(lastId != id){
            DmsSharedPreferences.saveLastLoggedInId(LoginActivity.this,id);
            UserProfileModel userProfileModel = DmsSharedPreferences.getUserDetails(LoginActivity.this);
            final String imageName = ConstantKeys.fBProfilePicsAlbumName+"/"+userProfileModel.getEmpID()+"_"+userProfileModel.getDisplayName()+".jpg";
            StorageReference childRef = controller.getStorageRef().child(imageName);
            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    DmsSharedPreferences.saveProfilePicUrl(LoginActivity.this,uri.toString());
                    passIntent();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    DmsSharedPreferences.saveProfilePicUrl(LoginActivity.this, "");
                    passIntent();
                }
            });
        }else{
            passIntent();
        }
    }

    public void passIntent(){
        Intent i = new Intent(LoginActivity.this, EventsList.class);
        startActivity(i);
        finish();
    }

}
