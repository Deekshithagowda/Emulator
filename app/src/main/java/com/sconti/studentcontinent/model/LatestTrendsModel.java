package com.sconti.studentcontinent.model;

import android.content.Context;

import java.util.List;

public class LatestTrendsModel {
    private String imageUrl;
    private String title;
    private String id;
    private String keywords;

    public LatestTrendsModel() {
    }

    public LatestTrendsModel(String imageUrl, String title, String id, String keywords) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.id = id;
        this.keywords = keywords;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
