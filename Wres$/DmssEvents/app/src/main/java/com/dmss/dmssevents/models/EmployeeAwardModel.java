package com.dmss.dmssevents.models;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 05-04-2017.
 */
public class EmployeeAwardModel {
int EmployeeId;
    String DisplayName="",EmpIdCard="",DeptName="",RoleName="",profilePic = "";
    public EmployeeAwardModel(JSONObject employeeJson) {
        try {
            EmployeeId = employeeJson.isNull("Id") ? 0 : employeeJson.getInt("Id");
            DisplayName = employeeJson.isNull("DisplayName") ? "" : employeeJson.getString("DisplayName");
            EmpIdCard = employeeJson.isNull("EmpID") ? "" : employeeJson.getString("EmpID");
            DeptName = employeeJson.isNull("DeptName") ? "" : employeeJson.getString("DeptName");
            RoleName = employeeJson.isNull("RoleName") ? "" : employeeJson.getString("RoleName");
            saveProfilePicUrl();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmpIdCard() {
        return EmpIdCard;
    }

    public void setEmpIdCard(String empIdCard) {
        EmpIdCard = empIdCard;
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
        final String imageName = "Profile_pics/" + EmpIdCard + "_" + DisplayName + ".jpg";
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
