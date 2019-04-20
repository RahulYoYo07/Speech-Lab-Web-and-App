package com.example.iitg_speech_lab.Model;


public class EnrollDataModel {

    String id;
    String name;
    String prof;
    String cinfo;

    public EnrollDataModel(String id, String name, String prof,String cinfo) {
        this.id = id;
        this.name = name;
        this.prof = prof;
        this.cinfo = cinfo;
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

    public  String getCourseInfo() { return cinfo; }
}
