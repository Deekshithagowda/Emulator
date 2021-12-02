package com.sconti.studentcontinent.activity.notifications.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserNotificationsModel implements Parcelable{
    private String commentDesc;
    private long id;
    private String postDescription;
    private String name;

    public UserNotificationsModel() {
    }

    public UserNotificationsModel(String commentDesc, long id, String postDescription, String name) {
        this.commentDesc = commentDesc;
        this.id = id;
        this.postDescription = postDescription;
        this.name = name;
    }


        protected UserNotificationsModel(Parcel in) {
            commentDesc = in.readString();
            id = in.readLong();
            postDescription = in.readString();
            name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(commentDesc);
            dest.writeLong(id);
            dest.writeString(postDescription);
            dest.writeString(name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserNotificationsModel> CREATOR = new Creator<UserNotificationsModel>() {
            @Override
            public UserNotificationsModel createFromParcel(Parcel in) {
                return new UserNotificationsModel(in);
            }

            @Override
            public UserNotificationsModel[] newArray(int size) {
                return new UserNotificationsModel[size];
            }
        };


        public String getCommentDesc() {
            return commentDesc;
        }

        public void setCommentDesc(String commentDesc) {
            this.commentDesc = commentDesc;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPostDescription() {
            return postDescription;
        }

        public void setPostDescription(String postDescription) {
            this.postDescription = postDescription;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Creator<UserNotificationsModel> getCREATOR() {
            return CREATOR;
        }
}
