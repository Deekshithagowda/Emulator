package com.sconti.studentcontinent.model;

public class TrendLongList {
    private String sl_no;
    private String description;
    private String numberOfLike;
    private String numberOfShares;
    private String numberOfComments;
    private String postId;
    private String sum;
    private String category;
    private String postedByName;
    private String profURL;
    public TrendLongList(){}

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getProfURL() {
        return profURL;
    }

    public void setProfURL(String profURL) {
        this.profURL = profURL;
    }

    public TrendLongList(String sl_no, String description, String numberOfLike, String numberOfShares, String numberOfComments, String postId, String sum, String category, String postedByName, String profURL) {
        this.sl_no = sl_no;
        this.description = description;
        this.numberOfLike = numberOfLike;
        this.numberOfShares = numberOfShares;
        this.numberOfComments = numberOfComments;
        this.postId = postId;
        this.sum = sum;
        this.category = category;
        this.postedByName = postedByName;
        this.profURL = profURL;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(String numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public String getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(String numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public String getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(String numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
