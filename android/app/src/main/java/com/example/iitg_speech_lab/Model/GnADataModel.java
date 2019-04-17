package com.example.iitg_speech_lab.Model;

public class GnADataModel {

        String name;
        String rollno;
        Long marks;
        Long attendance;

        public GnADataModel(String name, String rollno, Long marks, Long attendance) {
            this.name = name;
            this.rollno = rollno;
            this.marks = marks;
            this.attendance = attendance;
        }


        public Long getAttendance() {
            return attendance;
        }

        public Long getMarks() {
            return marks;
        }

        public String getName() {
            return name;
        }

        public String getRollNo() {
            return rollno;
        }
}
