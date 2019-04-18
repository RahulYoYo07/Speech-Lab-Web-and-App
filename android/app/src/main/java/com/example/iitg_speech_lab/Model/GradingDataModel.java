package com.example.iitg_speech_lab.Model;

public class GradingDataModel {

        String name;
        Integer grade;

        public GradingDataModel(String name, Integer grade) {
            this.name = name;
            this.grade = grade;
        }


        public String getName() {
            return name;
        }

        public Integer getGrade() {
            return grade;
        }
}
