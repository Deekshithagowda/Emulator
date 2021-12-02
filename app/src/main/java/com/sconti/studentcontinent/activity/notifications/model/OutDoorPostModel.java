package com.sconti.studentcontinent.activity.notifications.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OutDoorPostModel implements Parcelable {
    private String title;
    private String description;
    private String postedById;
    private String postedByName;
    private int mediaType;
    private String mediaURL;
    private String category;
    private long created_on;
    private long updated_on;
    private long sl_no;
    private String postId;
    private String category_id;
    private String prfurl;
    private long numLikes;
    private long numShares;
    private long numComments;
    private boolean isLiked;
    private boolean isCommented;
    private boolean isShared;

    public OutDoorPostModel() {
    }

    public OutDoorPostModel(String prfurl, String category_id, String postId, long sl_no, String title, String description, String postedById, String postedByName, int mediaType, String mediaURL, String category, long created_on, long updated_on, long numLikes, long numShares, long numComments) {
        this.title = title;
        this.description = description;
        this.postedById = postedById;
        this.postedByName = postedByName;
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
        this.category = category;
        this.created_on = created_on;
        this.updated_on = updated_on;
        this.sl_no = sl_no;
        this.postId = postId;
        this.category_id = category_id;
        this.prfurl = prfurl;
        this.numLikes = numLikes;
        this.numShares = numShares;
        this.numComments = numComments;
    }


    protected OutDoorPostModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        postedById = in.readString();
        postedByName = in.readString();
        mediaType = in.readInt();
        mediaURL = in.readString();
        category = in.readString();
        created_on = in.readLong();
        updated_on = in.readLong();
        sl_no = in.readLong();
        postId = in.readString();
        category_id = in.readString();
        prfurl = in.readString();
        numLikes = in.readLong();
        numShares = in.readLong();
        numComments = in.readLong();
        isLiked = in.readByte() != 0;
        isCommented = in.readByte() != 0;
        isShared = in.readByte() != 0;
    }

    public static final Creator<OutDoorPostModel> CREATOR = new Creator<OutDoorPostModel>() {
        @Override
        public OutDoorPostModel createFromParcel(Parcel in) {
            return new OutDoorPostModel(in);
        }

        @Override
        public OutDoorPostModel[] newArray(int size) {
            return new OutDoorPostModel[size];
        }
    };

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

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCreated_on() {
        return created_on;
    }

    public void setCreated_on(long created_on) {
        this.created_on = created_on;
    }

    public long getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(long updated_on) {
        this.updated_on = updated_on;
    }

    public long getSl_no() {
        return sl_no;
    }

    public void setSl_no(long sl_no) {
        this.sl_no = sl_no;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrfurl() {
        return prfurl;
    }

    public void setPrfurl(String prfurl) {
        this.prfurl = prfurl;
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

    public static Creator<OutDoorPostModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(postedById);
        parcel.writeString(postedByName);
        parcel.writeInt(mediaType);
        parcel.writeString(mediaURL);
        parcel.writeString(category);
        parcel.writeLong(created_on);
        parcel.writeLong(updated_on);
        parcel.writeLong(sl_no);
        parcel.writeString(postId);
        parcel.writeString(category_id);
        parcel.writeString(prfurl);
        parcel.writeLong(numLikes);
        parcel.writeLong(numShares);
        parcel.writeLong(numComments);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
        parcel.writeByte((byte) (isCommented ? 1 : 0));
        parcel.writeByte((byte) (isShared ? 1 : 0));
    }


}
