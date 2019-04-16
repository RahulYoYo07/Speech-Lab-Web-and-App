package com.example.iitg_speech_lab;

import java.util.HashMap;
import java.util.Map;

public class Courses {
    String Cname,Cid,CAbout,CEnroll;
    Integer Weight;
    Map<String,String> StartSemester = new HashMap<>();
    Map<String,String> EndSemester = new HashMap<>();

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getCAbout() {
        return CAbout;
    }

    public void setCAbout(String CAbout) {
        this.CAbout = CAbout;
    }

    public String getCEnroll() {
        return CEnroll;
    }

    public void setCEnroll(String CEnroll) {
        this.CEnroll = CEnroll;
    }

    public Integer getWeight() {
        return Weight;
    }

    public void setWeight(Integer weight) {
        Weight = weight;
    }

    public Map<String, String> getStartSemester() {
        return StartSemester;
    }

    public void setStartSemester(Map<String, String> startSemester) {
        StartSemester = startSemester;
    }

    public Map<String, String> getEndSemester() {
        return EndSemester;
    }

    public void setEndSemester(Map<String, String> endSemester) {
        EndSemester = endSemester;
    }

    public Courses(){

    }
    public Courses(String CAbout,String Cname,String Cid,String CEnroll,String CStartYear,String CStartType,String CEndYear,String CEndType,Integer Weight){
        this.Cname = Cname;
        this.Cid = Cid;
        this.CEnroll = CEnroll;
        this.CAbout = CAbout;
        this.Weight = Weight;
        this.StartSemester.put("SemesterType",CStartType);
        this.StartSemester.put("Session",CStartYear);
        this.EndSemester.put("SemesterType",CEndYear);
        this.EndSemester.put("Session",CEndType);
    }
}
