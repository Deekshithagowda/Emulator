package com.sconti.studentcontinent.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;

public class FirstPreferenceDataModel extends OutDoorPostModel implements Parcelable {
    private String imageURL;
    private String videoURL;
    private String title;
    private String description;
    private long datetime;
    private String postedBy;
    private String key;
    private String id;
    private long numLikes;
    private long numShares;
    private long numComments;
    private boolean isLiked;
    private boolean isCommented;
    private boolean isShared;
    private String posterUrl;
    private String postedByName;
    private String postedByCollege;

    public FirstPreferenceDataModel() {
    }

    public FirstPreferenceDataModel(String postedByCollege, String postedByName, String posterUrl, String imageURL, String videoURL, String title, String description, long datetime, String postedBy, String key, String id, long numLikes, long numShares, long numComments) {
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
        this.posterUrl = posterUrl;
        this.postedByName = postedByName;
        this.postedByCollege = postedByCollege;
    }


    protected FirstPreferenceDataModel(Parcel in) {
        imageURL = in.readString();
        videoURL = in.readString();
        title = in.readString();
        description = in.readString();
        datetime = in.readLong();
        postedBy = in.readString();
        key = in.readString();
        id = in.readString();
        numLikes = in.readLong();
        numShares = in.readLong();
        numComments = in.readLong();
        isLiked = in.readByte() != 0;
        isCommented = in.readByte() != 0;
        isShared = in.readByte() != 0;
        posterUrl = in.readString();
        postedByName = in.readString();
        postedByCollege = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeString(videoURL);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(datetime);
        dest.writeString(postedBy);
        dest.writeString(key);
        dest.writeString(id);
        dest.writeLong(numLikes);
        dest.writeLong(numShares);
        dest.writeLong(numComments);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeByte((byte) (isCommented ? 1 : 0));
        dest.writeByte((byte) (isShared ? 1 : 0));
        dest.writeString(posterUrl);
        dest.writeString(postedByName);
        dest.writeString(postedByCollege);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FirstPreferenceDataModel> CREATOR = new Creator<FirstPreferenceDataModel>() {
        @Override
        public FirstPreferenceDataModel createFromParcel(Parcel in) {
            return new FirstPreferenceDataModel(in);
        }

        @Override
        public FirstPreferenceDataModel[] newArray(int size) {
            return new FirstPreferenceDataModel[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        isCommented = commented;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getPostedByCollege() {
        return postedByCollege;
    }

    public void setPostedByCollege(String postedByCollege) {
        this.postedByCollege = postedByCollege;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }


}
