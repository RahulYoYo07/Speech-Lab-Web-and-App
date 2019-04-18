package com.example.iitg_speech_lab.Model;


public class StudentCoursesDataModel {

    String id;
    String name;
    String info;

    public StudentCoursesDataModel(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
