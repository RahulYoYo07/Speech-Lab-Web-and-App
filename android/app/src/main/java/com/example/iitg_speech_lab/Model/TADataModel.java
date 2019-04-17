package com.example.iitg_speech_lab.Model;

public class TADataModel {

        String name;
        String deadline;
        //replace by Deadline
        String info;

        public TADataModel(String info, String name, String deadline) {
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
