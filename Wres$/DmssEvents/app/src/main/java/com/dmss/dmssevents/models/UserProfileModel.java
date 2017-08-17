package com.dmss.dmssevents.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jaya.krishna on 20-Mar-17.
 */
public class UserProfileModel {
    String EmailID = "", EmpGuid = "", EmpID = "", FirstName = "", LastName = "", CommonName = "", DisplayName = "", ManagerGuid = "", JobTittle = "", BuisnessUnit = "", Department = "",
            UserRole = "", OfficePhone = "", Mobile = "",ProfilePhoto="";
    int Id;
    boolean Disabled, IsPasswordSet;

    public UserProfileModel(JSONObject userProfileResult) {
        try {
            //JSONObject jsonObject = new JSONObject(userProfileResult);
            Id = userProfileResult.isNull("Id") ? 0 : userProfileResult.getInt("Id");
            OfficePhone = userProfileResult.isNull("OfficePhone") ? "" : userProfileResult.getString("OfficePhone");
            Mobile = userProfileResult.isNull("Mobile") ? "" : userProfileResult.getString("Mobile");
            ProfilePhoto = userProfileResult.isNull("ProfilePhoto") ? "" : userProfileResult.getString("ProfilePhoto");
            EmailID = userProfileResult.isNull("EmailID") ? "" : userProfileResult.getString("EmailID");
            EmpGuid = userProfileResult.isNull("EmpGuid") ? "" : userProfileResult.getString("EmpGuid");
            EmpID = userProfileResult.isNull("EmpID") ? "" : userProfileResult.getString("EmpID");
            FirstName = userProfileResult.isNull("FirstName") ? "" : userProfileResult.getString("FirstName");
            LastName = userProfileResult.isNull("LastName") ? "" : userProfileResult.getString("LastName");
            CommonName = userProfileResult.isNull("CommonName") ? "" : userProfileResult.getString("CommonName");
            DisplayName = userProfileResult.isNull("DisplayName") ? "" : userProfileResult.getString("DisplayName");
            ManagerGuid = userProfileResult.isNull("ManagerGuid") ? "" : userProfileResult.getString("ManagerGuid");
            JobTittle = userProfileResult.isNull("JobTittle") ? "" : userProfileResult.getString("JobTittle");
            BuisnessUnit = userProfileResult.isNull("BuisnessUnit") ? "" : userProfileResult.getString("BuisnessUnit");
            Department = userProfileResult.isNull("Department") ? "" : userProfileResult.getString("Department");
            UserRole = userProfileResult.isNull("UserRole") ? "" : userProfileResult.getString("UserRole");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getEmpGuid() {
        return EmpGuid;
    }

    public void setEmpGuid(String empGuid) {
        EmpGuid = empGuid;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCommonName() {
        return CommonName;
    }

    public void setCommonName(String commonName) {
        CommonName = commonName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getManagerGuid() {
        return ManagerGuid;
    }

    public void setManagerGuid(String managerGuid) {
        ManagerGuid = managerGuid;
    }

    public String getJobTittle() {
        return JobTittle;
    }

    public void setJobTittle(String jobTittle) {
        JobTittle = jobTittle;
    }

    public String getBuisnessUnit() {
        return BuisnessUnit;
    }

    public void setBuisnessUnit(String buisnessUnit) {
        BuisnessUnit = buisnessUnit;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getOfficePhone() {
        return OfficePhone;
    }

    public void setOfficePhone(String officePhone) {
        OfficePhone = officePhone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public boolean isDisabled() {
        return Disabled;
    }

    public void setDisabled(boolean disabled) {
        Disabled = disabled;
    }

    public boolean isPasswordSet() {
        return IsPasswordSet;
    }

    public void setIsPasswordSet(boolean isPasswordSet) {
        IsPasswordSet = isPasswordSet;
    }
}
