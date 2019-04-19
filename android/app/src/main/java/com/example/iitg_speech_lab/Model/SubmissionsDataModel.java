package com.example.iitg_speech_lab.Model;

public class SubmissionsDataModel {

        String name;
        String url;
        String gid;

        public SubmissionsDataModel(String gid,String name, String url) {
            this.name = name;
            this.url = url;
            this.gid = gid;
        }

        public String getName() {
            return name;
        }

        public String getURL() {
            return url;
        }

        public String getGID() {
        return gid;
    }
}
