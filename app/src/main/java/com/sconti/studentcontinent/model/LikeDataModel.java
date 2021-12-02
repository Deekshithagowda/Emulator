package com.sconti.studentcontinent.model;

public class LikeDataModel {
    private String sl_no;
    private String likeCount;
    private String postID;
    private String likeByID;
    private String postedById;
    private String postedByName;

    public String getPostedById() {
        return postedById;
    }

    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public LikeDataModel(String sl_no, String likeCount, String postID, String likeByID, String postedById, String postedByName) {
        this.sl_no = sl_no;
        this.likeCount = likeCount;
        this.postID = postID;
        this.likeByID = likeByID;
        this.postedById = postedById;
        this.postedByName = postedByName;
    }

    public LikeDataModel(){

    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getLikeByID() {
        return likeByID;
    }

    public void setLikeByID(String likeByID) {
        this.likeByID = likeByID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
}
