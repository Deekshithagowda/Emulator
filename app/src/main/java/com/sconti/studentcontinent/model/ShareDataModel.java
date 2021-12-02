package com.sconti.studentcontinent.model;

public class ShareDataModel {
    private String sl_no;
    private String shareCount;
    private String postId;
    private String shareByID;
    private String category;
    private String postedById;
    private String postedByName;
    private String SharedByName;

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


    public ShareDataModel(String sl_no, String shareCount, String postId, String shareByID, String category, String postedById, String postedByName, String sharedByName) {
        this.sl_no = sl_no;
        this.shareCount = shareCount;
        this.postId = postId;
        this.shareByID = shareByID;
        this.category = category;
        this.postedById = postedById;
        this.postedByName = postedByName;
        SharedByName = sharedByName;
    }

    public String getSharedByName() {
        return SharedByName;
    }

    public void setSharedByName(String sharedByName) {
        SharedByName = sharedByName;
    }

    public ShareDataModel() {
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getShareByID() {
        return shareByID;
    }

    public void setShareByID(String shareByID) {
        this.shareByID = shareByID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
