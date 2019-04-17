package com.example.iitg_speech_lab.Model;

public class GroupsDataModel {

        String name;
        String deadline;
        //replace by Deadline
        String info;

        public GroupsDataModel(String info, String name, String deadline) {
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
