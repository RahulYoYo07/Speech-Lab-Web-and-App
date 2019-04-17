package com.example.iitg_speech_lab.Model;

public class StudyMaterialDataModel {

    String name;
    String url;

    public StudyMaterialDataModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}