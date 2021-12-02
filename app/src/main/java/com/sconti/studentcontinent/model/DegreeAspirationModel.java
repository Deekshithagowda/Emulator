package com.sconti.studentcontinent.model;

public class DegreeAspirationModel {
    private String name;
    private String id;

    public DegreeAspirationModel() {
    }

    public DegreeAspirationModel(String name, String id) {
        this.name = name;
        this.id = id;
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
}
