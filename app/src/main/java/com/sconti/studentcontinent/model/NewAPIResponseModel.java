package com.sconti.studentcontinent.model;

import com.sconti.studentcontinent.activity.outdoor.model.OutDoorHashTagModel;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;

import java.util.List;

public class NewAPIResponseModel {

    boolean status;
    int statusCode;
    String message;
    UserDetails mUserDetails;
    List<UserDetails> usertable;
    List<CategoryModel> categoryModel;
    List<LevelOneModel> levelOneModel;
    List<OutDoorHashTagModel> hashTagsModel;
    List<OutDoorPostModel> knowledgeModel;
    List<LikeDataModel> knlowledgeLikeModel;
    List<ShareDataModel> knowledgeShareModel;
    List<ShareDataModel> shareDataList;
    List<LikeDataModel> addKnowledgeLikeModel;
    List<LikeDataModel> addKnowledgeGenLikeModel;
    List<OutDoorPostModel> outDoorModel;
    List<FollowModel> mFollowList;
    List<CommentModel> commentModel;
    List<CommentModel> commentDataList;
    List<LikeDataModel> likeDatalist;
    List<NotificationDataModel> mNotificationDataList;
    List<TrendLongList> trendLingList;

    public NewAPIResponseModel() {
    }


    public List<CommentModel> getCommentDataList() {
        return commentDataList;
    }

    public void setCommentDataList(List<CommentModel> commentDataList) {
        this.commentDataList = commentDataList;
    }

    public List<ShareDataModel> getShareDataList() {
        return shareDataList;
    }

    public void setShareDataList(List<ShareDataModel> shareDataList) {
        this.shareDataList = shareDataList;
    }

    public NewAPIResponseModel(boolean status, int statusCode, String message, UserDetails mUserDetails, List<UserDetails> usertable, List<CategoryModel> categoryModel, List<LevelOneModel> levelOneModel, List<OutDoorHashTagModel> hashTagsModel, List<OutDoorPostModel> knowledgeModel, List<LikeDataModel> knlowledgeLikeModel, List<ShareDataModel> knowledgeShareModel, List<ShareDataModel> shareDataList, List<LikeDataModel> addKnowledgeLikeModel, List<LikeDataModel> addKnowledgeGenLikeModel, List<OutDoorPostModel> outDoorModel, List<FollowModel> mFollowList, List<CommentModel> commentModel, List<CommentModel> commentDataList, List<LikeDataModel> likeDatalist, List<NotificationDataModel> mNotificationDataList, List<TrendLongList> trendLingList) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.mUserDetails = mUserDetails;
        this.usertable = usertable;
        this.categoryModel = categoryModel;
        this.levelOneModel = levelOneModel;
        this.hashTagsModel = hashTagsModel;
        this.knowledgeModel = knowledgeModel;
        this.knlowledgeLikeModel = knlowledgeLikeModel;
        this.knowledgeShareModel = knowledgeShareModel;
        this.shareDataList = shareDataList;
        this.addKnowledgeLikeModel = addKnowledgeLikeModel;
        this.addKnowledgeGenLikeModel = addKnowledgeGenLikeModel;
        this.outDoorModel = outDoorModel;
        this.mFollowList = mFollowList;
        this.commentModel = commentModel;
        this.commentDataList = commentDataList;
        this.likeDatalist = likeDatalist;
        this.mNotificationDataList = mNotificationDataList;
        this.trendLingList = trendLingList;
    }

    public List<TrendLongList> getTrendLingList() {
        return trendLingList;
    }

    public void setTrendLingList(List<TrendLongList> trendLingList) {
        this.trendLingList = trendLingList;
    }

    public boolean isStatus() {
        return status;
    }

    public List<LikeDataModel> getLikeDatalist() {
        return likeDatalist;
    }

    public void setLikeDatalist(List<LikeDataModel> likeDatalist) {
        this.likeDatalist = likeDatalist;
    }

    public List<NotificationDataModel> getmNotificationDataList() {
        return mNotificationDataList;
    }

    public void setmNotificationDataList(List<NotificationDataModel> mNotificationDataList) {
        this.mNotificationDataList = mNotificationDataList;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<CategoryModel> getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(List<CategoryModel> categoryModel) {
        this.categoryModel = categoryModel;
    }

    public List<LevelOneModel> getLevelOneModel() {
        return levelOneModel;
    }

    public void setLevelOneModel(List<LevelOneModel> levelOneModel) {
        this.levelOneModel = levelOneModel;
    }

    public List<OutDoorHashTagModel> getHashTagsModel() {
        return hashTagsModel;
    }

    public void setHashTagsModel(List<OutDoorHashTagModel> hashTagsModel) {
        this.hashTagsModel = hashTagsModel;
    }

    public List<OutDoorPostModel> getKnowledgeModel() {
        return knowledgeModel;
    }

    public void setKnowledgeModel(List<OutDoorPostModel> knowledgeModel) {
        this.knowledgeModel = knowledgeModel;
    }

    public List<LikeDataModel> getKnlowledgeLikeModel() {
        return knlowledgeLikeModel;
    }

    public void setKnlowledgeLikeModel(List<LikeDataModel> knlowledgeLikeModel) {
        this.knlowledgeLikeModel = knlowledgeLikeModel;
    }

    public List<ShareDataModel> getKnowledgeShareModel() {
        return knowledgeShareModel;
    }

    public void setKnowledgeShareModel(List<ShareDataModel> knowledgeShareModel) {
        this.knowledgeShareModel = knowledgeShareModel;
    }

    public List<LikeDataModel> getAddKnowledgeLikeModel() {
        return addKnowledgeLikeModel;
    }

    public void setAddKnowledgeLikeModel(List<LikeDataModel> addKnowledgeLikeModel) {
        this.addKnowledgeLikeModel = addKnowledgeLikeModel;
    }

    public List<LikeDataModel> getAddKnowledgeGenLikeModel() {
        return addKnowledgeGenLikeModel;
    }

    public void setAddKnowledgeGenLikeModel(List<LikeDataModel> addKnowledgeGenLikeModel) {
        this.addKnowledgeGenLikeModel = addKnowledgeGenLikeModel;
    }

    public List<OutDoorPostModel> getOutDoorModel() {
        return outDoorModel;
    }

    public void setOutDoorModel(List<OutDoorPostModel> outDoorModel) {
        this.outDoorModel = outDoorModel;
    }

    public List<FollowModel> getmFollowList() {
        return mFollowList;
    }

    public void setmFollowList(List<FollowModel> mFollowList) {
        this.mFollowList = mFollowList;
    }

    public List<CommentModel> getCommentModel() {
        return commentModel;
    }

    public void setCommentModel(List<CommentModel> commentModel) {
        this.commentModel = commentModel;
    }

    public List<NotificationDataModel> getNotificationDataModelList() {
        return mNotificationDataList;
    }

    public void setNotificationDataModelList(List<NotificationDataModel> notificationDataModelList) {
        this.mNotificationDataList = notificationDataModelList;
    }
}
