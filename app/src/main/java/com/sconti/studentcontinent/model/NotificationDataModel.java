package com.sconti.studentcontinent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationDataModel implements Parcelable {
    private String Description;
    private String postID;
    private String commentedByUserID;
    private String postOwnerID;
    private String created_on;
    private String CommentByName;
    private String profileURL;
    private int isRead;
    private int isDeleted;
    private int type;
    private String sl_no;

    public NotificationDataModel(Parcel in) {
        Description = in.readString();
        postID = in.readString();
        commentedByUserID = in.readString();
        postOwnerID = in.readString();
        created_on = in.readString();
        CommentByName = in.readString();
        profileURL = in.readString();
        type = in.readInt();
        isRead = in.readInt();
        isDeleted = in.readInt();
        sl_no = in.readString();
    }

    public NotificationDataModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeString(postID);
        dest.writeString(commentedByUserID);
        dest.writeString(postOwnerID);
        dest.writeString(created_on);
        dest.writeString(CommentByName);
        dest.writeString(profileURL);
        dest.writeInt(type);
        dest.writeInt(isRead);
        dest.writeInt(isDeleted);
        dest.writeString(sl_no);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationDataModel> CREATOR = new Creator<NotificationDataModel>() {
        @Override
        public NotificationDataModel createFromParcel(Parcel in) {
            return new NotificationDataModel(in);
        }

        @Override
        public NotificationDataModel[] newArray(int size) {
            return new NotificationDataModel[size];
        }
    };


    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public String getPostID() {
        return postID;
    }
    public void setPostID(String postID) {
        this.postID = postID;
    }
    public String getCommentedByUserID() {
        return commentedByUserID;
    }
    public void setCommentedByUserID(String commentedByUserID) {
        this.commentedByUserID = commentedByUserID;
    }
    public String getPostOwnerID() {
        return postOwnerID;
    }
    public void setPostOwnerID(String postOwnerID) {
        this.postOwnerID = postOwnerID;
    }
    public String getCreated_on() {
        return created_on;
    }
    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
    public String getName() {
        return CommentByName;
    }
    public void setName(String name) {
        this.CommentByName = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
