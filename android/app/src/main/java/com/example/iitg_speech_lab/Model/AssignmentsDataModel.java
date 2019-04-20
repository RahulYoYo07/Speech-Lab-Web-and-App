package com.example.iitg_speech_lab.Model;

import com.google.type.Date;

public class AssignmentsDataModel {

        String name;
        String deadline;
        //replace by Deadline
        String info;

        public AssignmentsDataModel( String info, String name,String deadline) {
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
