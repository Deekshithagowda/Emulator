package com.sconti.studentcontinent.model;

import java.util.List;

public class LikeModel {
    private String postID;
    private String sl_no;
    private String likeByID;
    private String likeCount;
    private String postedById;
    private String postedByName;
    private String  likedByName;
    private String  sharedByName;
    private List<UserDetails> likedUsersList;
    private boolean isLiked;

    public LikeModel() {
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

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

    public String getLikedByName() {
        return likedByName;
    }

    public void setLikedByName(String likedByName) {
        this.likedByName = likedByName;
    }

    public String getSharedByName() {
        return sharedByName;
    }

    public void setSharedByName(String sharedByName) {
        this.sharedByName = sharedByName;
    }

    public LikeModel(String postID, String sl_no, String likeByID, String likeCount, String postedById, String postedByName, String likedByName, String sharedByName, List<UserDetails> likedUsersList, boolean isLiked) {
        this.postID = postID;
        this.sl_no = sl_no;
        this.likeByID = likeByID;
        this.likeCount = likeCount;
        this.postedById = postedById;
        this.postedByName = postedByName;
        this.likedByName = likedByName;
        this.sharedByName = sharedByName;
        this.likedUsersList = likedUsersList;
        this.isLiked = isLiked;
    }

    public String getPostId() {
        return postID;
    }

    public void setPostId(String postId) {
        this.postID = postId;
    }

    public String getLikeByID() {
        return likeByID;
    }

    public void setLikeByID(String likeByID) {
        this.likeByID = likeByID;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public List<UserDetails> getLikedUsersList() {
        return likedUsersList;
    }

    public void setLikedUsersList(List<UserDetails> likedUsersList) {
        this.likedUsersList = likedUsersList;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
