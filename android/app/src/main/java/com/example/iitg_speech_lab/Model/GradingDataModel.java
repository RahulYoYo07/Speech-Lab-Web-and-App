package com.example.iitg_speech_lab.Model;

public class GradingDataModel {

        String name;
        String deadline;
        //replace by Deadline
        String info;

        public GradingDataModel(String info, String name, String deadline) {
            this.deadline = deadline;
            this.name = name;
            this.info = info;
        }


        public String getDeadline() {
            return deadline;
        }

        public String getName() {
            return name;
        }

        public String getInfo() {
            return info;
        }
}
