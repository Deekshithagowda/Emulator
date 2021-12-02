package com.sconti.studentcontinent.model;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails{
    private String name;
    private String phone;
    private String interest;
    private String email;
    private String fcm_token;
    private String pwd;
    private String imageURL;
    private String userId;
    private String village;
    private String degree;
    private String mobile;
    private boolean status;
    private String msg;
    private String id;
    private String selectedDegree, selectedDegreeName;
    private String interestName;
    private String college;

    public UserDetails() {
        super();
    }

    public UserDetails(String name, String phone, String interest, String email, String fcm_token, String pwd,
                       String imageURL, String userId, String village, String degree) {
        super();
        this.name = name;
        this.phone = phone;
        this.interest = interest;
        this.email = email;
        this.fcm_token = fcm_token;
        this.pwd = pwd;
        this.imageURL = imageURL;
        this.userId = userId;
        this.village = village;
        this.degree = degree;
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

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelectedDegree() {
        return selectedDegree;
    }

    public void setSelectedDegree(String selectedDegree) {
        this.selectedDegree = selectedDegree;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}