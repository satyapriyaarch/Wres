package com.dmss.dmssevents.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 05-04-2017.
 */
public class AwardsModel {
int Id,EmployeeId;
    String AwardName="",Description="",Criteria="",Rewards="",ColorCode="",awardImage = "";
    boolean IsActive;

    ArrayList<EmployeeAwardModel> employeeAwardModelArrayList=new ArrayList<EmployeeAwardModel>();

    public AwardsModel(JSONObject resultJson){
    try{
        Id=resultJson.isNull("Id")?0:resultJson.getInt("Id");
        AwardName=resultJson.isNull("AwardName")?"":resultJson.getString("AwardName");
        Description=resultJson.isNull("Description")?"":resultJson.getString("Description");
        Criteria=resultJson.isNull("Criteria")?"":resultJson.getString("Criteria");
        Rewards=resultJson.isNull("Rewards")?"":resultJson.getString("Rewards");
        ColorCode=resultJson.isNull("ColorCode")?"":resultJson.getString("ColorCode");
        IsActive=resultJson.isNull("IsActive")?false:resultJson.getBoolean("IsActive");
        if (!resultJson.isNull("Employee")) {
            JSONArray employeeJsonArray=resultJson.getJSONArray("Employee");
            for (int i = 0; i < employeeJsonArray.length(); i++) {
                EmployeeAwardModel employeeAwardModel = new EmployeeAwardModel(employeeJsonArray.getJSONObject(i));
                employeeAwardModelArrayList.add(employeeAwardModel);
            }
            //EmployeeId=employeeJson.isNull("Id")?0:employeeJson.getInt("Id");
            /*DisplayName=employeeJson.isNull("DisplayName")?"":employeeJson.getString("DisplayName");
            EmpIdCard=employeeJson.isNull("EmpID")?"":employeeJson.getString("EmpID");
            DeptName=employeeJson.isNull("DeptName")?"":employeeJson.getString("DeptName");
            RoleName=employeeJson.isNull("RoleName")?"":employeeJson.getString("RoleName");*/

        }
    }catch (Exception ex){
        ex.printStackTrace();
    }
    }

    public String getAwardImage() {
        return awardImage;
    }

    public void setAwardImage(String awardImage) {
        this.awardImage = awardImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    public String getAwardName() {
        return AwardName;
    }

    public void setAwardName(String awardName) {
        AwardName = awardName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCriteria() {
        return Criteria;
    }

    public void setCriteria(String criteria) {
        Criteria = criteria;
    }

    public String getRewards() {
        return Rewards;
    }

    public void setRewards(String rewards) {
        Rewards = rewards;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }


    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public ArrayList<EmployeeAwardModel> getEmployeeAwardModelArrayList() {
        return employeeAwardModelArrayList;
    }

    public void setEmployeeAwardModelArrayList(ArrayList<EmployeeAwardModel> employeeAwardModelArrayList) {
        this.employeeAwardModelArrayList = employeeAwardModelArrayList;
    }
}

