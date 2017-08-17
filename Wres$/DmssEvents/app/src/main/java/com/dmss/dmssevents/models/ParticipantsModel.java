package com.dmss.dmssevents.models;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 31-03-2017.
 */
public class ParticipantsModel {
    int Id;
    String DisplayName = "", EmpID = "", DeptName = "", RoleName = "", profilePic = "";


    public ParticipantsModel(JSONObject resultJson) {
        if (resultJson != null) {
            try {
                Id = resultJson.isNull("Id") ? 0 : resultJson.getInt("Id");
                DisplayName = resultJson.isNull("DisplayName") ? "" : resultJson.getString("DisplayName");
                EmpID = resultJson.isNull("EmpID") ? "" : resultJson.getString("EmpID");
                DeptName = resultJson.isNull("DeptName") ? "" : resultJson.getString("DeptName");
                RoleName = resultJson.isNull("RoleName") ? "" : resultJson.getString("RoleName");
                saveProfilePicUrl();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void saveProfilePicUrl() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dmssevents-606bf.appspot.com");
        final String imageName = "Profile_pics/" + EmpID + "_" + DisplayName + ".jpg";
        StorageReference childRef = storageRef.child(imageName);
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

    }
}
