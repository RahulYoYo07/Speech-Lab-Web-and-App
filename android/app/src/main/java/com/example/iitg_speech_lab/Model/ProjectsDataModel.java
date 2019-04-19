package com.example.iitg_speech_lab.Model;


public class ProjectsDataModel {

    String id;
    String name;
    String info;

    public ProjectsDataModel(String id, String name, String info) {
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
