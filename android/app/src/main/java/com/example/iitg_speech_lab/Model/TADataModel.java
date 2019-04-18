package com.example.iitg_speech_lab.Model;

public class TADataModel {

        String name;
        String program;
        String TAinfo;

        public TADataModel(String name, String program, String TAinfo) {
            this.name = name;
            this.program = program;
            this.TAinfo = TAinfo;
        }

        public String getName() {
            return name;
        }

        public String getProgram() {
            return program;
        }

        public String getTAinfo() { return TAinfo; }
}
