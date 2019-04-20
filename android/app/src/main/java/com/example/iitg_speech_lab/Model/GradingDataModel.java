package com.example.iitg_speech_lab.Model;

public class GradingDataModel {

    String name;
    long grade;


    public GradingDataModel(String name, Long grade) {
        this.name = name;
        this.grade = grade;
    }


    public String getName() {
        return name;
    }

    public Long getGrade() {
        return grade;
    }
}