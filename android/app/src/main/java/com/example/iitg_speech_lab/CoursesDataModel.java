package com.example.iitg_speech_lab;


public class CoursesDataModel {

    String name;
    String version;
    int id_;

    public CoursesDataModel(String name, String version, int id) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
    }


    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }


    public int getId() {
        return id_;
    }
}
