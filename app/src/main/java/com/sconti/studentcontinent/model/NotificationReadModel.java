package com.sconti.studentcontinent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationReadModel implements Parcelable {

    private String NotificationID;
    private int status;
    private int type;

    protected NotificationReadModel(Parcel in) {
        NotificationID = in.readString();
        status = in.readInt();
        type = in.readInt();
    }

    public static final Creator<NotificationReadModel> CREATOR = new Creator<NotificationReadModel>() {
        @Override
        public NotificationReadModel createFromParcel(Parcel in) {
            return new NotificationReadModel(in);
        }

        @Override
        public NotificationReadModel[] newArray(int size) {
            return new NotificationReadModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(NotificationID);
        dest.writeInt(status);
        dest.writeInt(type);
    }

    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Creator<NotificationReadModel> getCREATOR() {
        return CREATOR;
    }
}
