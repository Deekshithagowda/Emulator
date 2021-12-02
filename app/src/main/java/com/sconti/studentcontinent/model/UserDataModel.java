package com.sconti.studentcontinent.model;

public class UserDataModel {
    private String name;
    private String mobile;
    private String interest;
    private String email;
    private boolean isFocusSelected;
    private String focusSubject;
    private String userID;
    private String profileURL;

    public UserDataModel() {
    }

    public UserDataModel(String profileURL, String userID, String name, String mobile, String interest, String email, boolean isFocusSelected, String focusSubject) {
        this.name = name;
        this.mobile = mobile;
        this.interest = interest;
        this.email = email;
        this.isFocusSelected = isFocusSelected;
        this.focusSubject = focusSubject;
        this.userID = userID;
        this.profileURL = profileURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFocusSelected() {
        return isFocusSelected;
    }

    public void setFocusSelected(boolean focusSelected) {
        isFocusSelected = focusSelected;
    }

    public String getFocuseSubject() {
        return focusSubject;
    }

    public void setFocuseSubject(String focusSubject) {
        this.focusSubject = focusSubject;
    }

    public String getFocusSubject() {
        return focusSubject;
    }

    public void setFocusSubject(String focusSubject) {
        this.focusSubject = focusSubject;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
