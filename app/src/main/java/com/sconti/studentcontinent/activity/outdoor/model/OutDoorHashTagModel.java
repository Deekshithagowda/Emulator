package com.sconti.studentcontinent.activity.outdoor.model;

import java.util.List;

public class OutDoorHashTagModel {
    private long sl_no;
    private String id;
    private String name;
    private long created_on;
    private long updated_on;
    private List<OutDoorPostModel> OutDoorPostData;

    public OutDoorHashTagModel() {
    }

    public OutDoorHashTagModel(long sl_no, String id, String name, long created_on, long updated_on, List<OutDoorPostModel> outDoorPostData) {
        this.sl_no = sl_no;
        this.id = id;
        this.name = name;
        this.created_on = created_on;
        this.updated_on = updated_on;
        OutDoorPostData = outDoorPostData;
    }

    public long getSl_no() {
        return sl_no;
    }

    public void setSl_no(long sl_no) {
        this.sl_no = sl_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<OutDoorPostModel> getOutDoorPostData() {
        return OutDoorPostData;
    }

    public void setOutDoorPostData(List<OutDoorPostModel> outDoorPostData) {
        OutDoorPostData = outDoorPostData;
    }
}
