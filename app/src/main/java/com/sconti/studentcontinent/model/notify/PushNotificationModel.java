package com.sconti.studentcontinent.model.notify;

public class PushNotificationModel {
    private String title;
    private String msg;

    public PushNotificationModel(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }

    public PushNotificationModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
