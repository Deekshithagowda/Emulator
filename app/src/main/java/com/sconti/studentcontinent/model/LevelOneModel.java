package com.sconti.studentcontinent.model;

public class LevelOneModel {
    private String l1_sl_no;
    private String name;
    private String category;
    private String icon;
    private String created_on;
    private String updated_on;

    public LevelOneModel() {
    }

    public LevelOneModel(String l1_sl_no, String name, String category, String icon, String created_on, String updated_on) {
        this.l1_sl_no = l1_sl_no;
        this.name = name;
        this.category = category;
        this.icon = icon;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public String getL1_sl_no() {
        return l1_sl_no;
    }

    public void setL1_sl_no(String l1_sl_no) {
        this.l1_sl_no = l1_sl_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}
