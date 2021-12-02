package com.sconti.studentcontinent.model;

public class SubjectListModel {
    private String name;
    private String id;
    private String key;

    public SubjectListModel(String name, String id, String key) {
        this.name = name;
        this.id = id;
        this.key = key;
    }

    public SubjectListModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
