package com.sconti.studentcontinent.model;

public class UserResponseModel {
    private boolean isLiked;
    private boolean isCommented;
    private boolean isShared;
    private String id;

    public UserResponseModel() {
    }

    public UserResponseModel(String id, boolean isLiked, boolean isCommented, boolean isShared) {
        this.isLiked = isLiked;
        this.isCommented = isCommented;
        this.isShared = isShared;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
