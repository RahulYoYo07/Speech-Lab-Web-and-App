package com.example.iitg_speech_lab.Model;


public class EnrollDataModel {

    String id;
    String name;
    String prof;

    public EnrollDataModel(String id, String name, String prof) {
        this.id = id;
        this.name = name;
        this.prof = prof;
    }

    public String getCourseId() {
        return id;
    }

    public String getCourseName() {
        return name;
    }

    public String getProfName() {
        return prof;
    }
}
