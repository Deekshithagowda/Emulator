package com.sconti.studentcontinent.model;

public class CategoryModel {
    private String c_sl_no;
    private String cat_name;
    private String options;
    private String icon;
    private String created_on;
    private String updated_on;

    public CategoryModel() {
    }

    public CategoryModel(String c_sl_no, String cat_name, String options, String icon, String created_on, String updated_on) {
        this.c_sl_no = c_sl_no;
        this.cat_name = cat_name;
        this.options = options;
        this.icon = icon;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public String getC_sl_no() {
        return c_sl_no;
    }

    public void setC_sl_no(String c_sl_no) {
        this.c_sl_no = c_sl_no;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
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
