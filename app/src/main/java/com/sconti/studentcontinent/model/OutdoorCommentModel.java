package com.sconti.studentcontinent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OutdoorCommentModel implements Parcelable {
    private String imageURL;
    private String videoURL;
    private String title;
    private String description;
    private String datetime;
    private String postedBy;
    private String key;
    private String id;
    private long numLikes;
    private long numShares;
    private long numComments;
    private String commentedById;
    private String commentedByName;
    private String commentID;
    private String commentDesc;
    private String commentorURL;

    public OutdoorCommentModel() {
    }

    public OutdoorCommentModel(String commentorURL,String commentDesc, String imageURL, String videoURL, String title, String description, String datetime, String postedBy, String key, String id, long numLikes, long numShares, long numComments, String commentedById, String commentedByName, String commentID) {
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.postedBy = postedBy;
        this.key = key;
        this.id = id;
        this.numLikes = numLikes;
        this.numShares = numShares;
        this.numComments = numComments;
        this.commentedById = commentedById;
        this.commentedByName = commentedByName;
        this.commentID = commentID;
        this.commentDesc = commentDesc;
        this.commentorURL = commentorURL;
    }

    protected OutdoorCommentModel(Parcel in) {
        imageURL = in.readString();
        videoURL = in.readString();
        title = in.readString();
        description = in.readString();
        datetime = in.readString();
        postedBy = in.readString();
        key = in.readString();
        id = in.readString();
        numLikes = in.readLong();
        numShares = in.readLong();
        numComments = in.readLong();
        commentedById = in.readString();
        commentedByName = in.readString();
        commentID = in.readString();
        commentDesc = in.readString();
        commentorURL = in.readString();
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageURL);
        parcel.writeString(videoURL);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(datetime);
        parcel.writeString(postedBy);
        parcel.writeString(key);
        parcel.writeString(id);
        parcel.writeLong(numLikes);
        parcel.writeLong(numShares);
        parcel.writeLong(numComments);
        parcel.writeString(commentedById);
        parcel.writeString(commentedByName);
        parcel.writeString(commentID);
        parcel.writeString(commentDesc);
        parcel.writeString(commentorURL);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommentorURL() {
        return commentorURL;
    }

    public void setCommentorURL(String commentorURL) {
        this.commentorURL = commentorURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public long getNumShares() {
        return numShares;
    }

    public void setNumShares(long numShares) {
        this.numShares = numShares;
    }

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public String getCommentedById() {
        return commentedById;
    }

    public void setCommentedById(String commentedById) {
        this.commentedById = commentedById;
    }

    public String getCommentedByName() {
        return commentedByName;
    }

    public void setCommentedByName(String commentedByName) {
        this.commentedByName = commentedByName;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public static Creator<CommentModel> getCREATOR() {
        return CREATOR;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }
}

