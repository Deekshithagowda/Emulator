package com.sconti.studentcontinent.model;

import java.util.Objects;

public class FollowModel {
    private String followers_id;
    private String followee_id;

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

    public FollowModel() {
    }

    public FollowModel(String followers_id, String followee_id, String name, String phone, String interest, String email, String fcm_token, String pwd, String imageURL, String userId, String village, String degree) {
        this.followers_id = followers_id;
        this.followee_id = followee_id;
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

    public String getFollower_id() {
        return followers_id;
    }

    public void setFollower_id(String follower_id) {
        this.followers_id = follower_id;
    }

    public String getFollowee_id() {
        return followee_id;
    }

    public void setFollowee_id(String followee_id) {
        this.followee_id = followee_id;
    }

    public String getFollowers_id() {
        return followers_id;
    }

    public void setFollowers_id(String followers_id) {
        this.followers_id = followers_id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowModel p = (FollowModel) o;
        return this.userId.equals(p.userId);
        // return this.id == p.id /&& Objects.equals(name, p.name)/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
