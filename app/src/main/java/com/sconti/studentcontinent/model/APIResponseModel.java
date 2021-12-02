package com.sconti.studentcontinent.model;

import java.util.List;

public class APIResponseModel {
    private boolean status;
    private String msg;
    private String name;
    private String phone;
    private String id;
    private String email;
    private String imageURL;
    private String selectedDegree, selectedDegreeName;
    private String interest, interestName;
    private String college;
    private UserDetails mUserDetails;
    private List<DegreeAspirationModel> degreeModel;

    private List<UserDetails> usertable;
    public APIResponseModel() {
    }

    public APIResponseModel(boolean status, String msg, String name, String phone, String id, String email, String imageURL, String selectedDegree, String degreeName, String interest, String aspirationName, String college) {
        this.status = status;
        this.msg = msg;
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.email = email;
        this.imageURL = imageURL;
        this.selectedDegree = selectedDegree;
        this.selectedDegreeName = degreeName;
        this.interest = interest;
        this.interestName = aspirationName;
        this.college = college;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSelectedDegree() {
        return selectedDegree;
    }

    public void setSelectedDegree(String selectedDegree) {
        this.selectedDegree = selectedDegree;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getVillage() {
        return college;
    }

    public void setVillage(String village) {
        this.college = village;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSelectedDegreeName() {
        return selectedDegreeName;
    }

    public void setSelectedDegreeName(String selectedDegreeName) {
        this.selectedDegreeName = selectedDegreeName;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public UserDetails getmUserDetails() {
        return mUserDetails;
    }

    public void setmUserDetails(UserDetails mUserDetails) {
        this.mUserDetails = mUserDetails;
    }

    public List<UserDetails> getUsertable() {
        return usertable;
    }

    public void setUsertable(List<UserDetails> usertable) {
        this.usertable = usertable;
    }

    public List<DegreeAspirationModel> getDegreeAspirationModelList() {
        return degreeModel;
    }

    public void setDegreeAspirationModelList(List<DegreeAspirationModel> degreeAspirationModelList) {
        this.degreeModel = degreeAspirationModelList;
    }
}
