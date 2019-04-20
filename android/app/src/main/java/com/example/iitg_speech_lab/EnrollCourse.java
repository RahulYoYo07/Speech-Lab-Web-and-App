package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.format.InputAccessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnrollCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_course);
    }
    public static Integer Check = 0 ;
    public void Add_Grade_Group(final View view){
        final String CourseInfo = getIntent().getStringExtra("courseInfo");
        final String Username =StudentCoursesActivity.username;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference assignsRef = db.collection("Courses").document(CourseInfo);
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            String EnrollmentKey = (String) doc.get("EnrollmentKey");
                            EditText ET = findViewById(R.id.enrollkey);
                            String EnrollKey1 = ET.getText().toString();
                            ArrayList<Map<Object,Object>> StdList = new ArrayList<Map<Object,Object>>();
                            ArrayList<Map<Object,Object>> AttnList = new ArrayList<Map<Object,Object>>();
                            try {
                                ArrayList<Map<Object,Object>> StdLis = (ArrayList<Map<Object, Object>>) doc.get("StudentList");
                                StdList = StdLis;
                            }
                            catch (Exception e) {
                                System.out.println("eedbdbqwdigdigqduigqwidugquigd");
                            }
                            try {
                                ArrayList<Map<Object,Object>> StdLispy = (ArrayList<Map<Object,Object>>) doc.get("AttendanceList");
                                StdList = StdLispy;
                            }
                            catch (Exception e) {
                                System.out.println("eedbdbqwdigdigqduigqwidugquigd");
                            }
                            if (EnrollKey1.equals(EnrollmentKey)) {
                                FirebaseFirestore dc = FirebaseFirestore.getInstance();
                                final DocumentReference usr = dc.collection("Users").document(Username);
                                Map<Object,Object> mp1 = new HashMap<>();
                                Map<Object,Object> mp2 = new HashMap<>();
                                final Map<Object,Object> mp3 = new HashMap<>();
                                mp1.put("Grade",0);
                                mp1.put("StudentID",usr);
                                mp2.put("TotalAttendance",0);
                                mp2.put("StudentID",usr);
                                mp3.put("CourseID",assignsRef);
                                ArrayList<Map<Object,Object>> xrd = new ArrayList<Map<Object,Object>>();
                                if (StdList != null){
                                    xrd = StdList;
                                }
                                xrd.add(mp1);
                                ArrayList<Map<Object,Object>> xrdud = new ArrayList<Map<Object,Object>>();
                                if (AttnList != null) {
                                    xrdud = AttnList;
                                }
                                xrdud.add(mp2);
                                Map<String,Object> pushdata = new HashMap<>();
                                pushdata.put("AttendanceList",xrdud);
                                pushdata.put("StudentList",xrd);

                                System.out.print(pushdata);
                                System.out.println("addcdcdvvv");
                                dc.collection("Courses").document(CourseInfo).update(pushdata);
                                usr.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot df = task.getResult();
                                                    ArrayList<Map<Object,Object>> curselist = (ArrayList<Map<Object,Object>>) df.get("CourseList");

                                                    ArrayList<Map<Object,Object>> ganndu = new ArrayList<Map<Object,Object>>();
                                                    if (curselist != null ){
                                                        ganndu = curselist;
                                                    }
                                                    ganndu.add(mp3);
                                                    Map<String,Object> cursedata = new HashMap<>();
                                                    cursedata.put("CourseList",ganndu);
                                                    System.out.print(cursedata);
                                                    System.out.println("addcdcdvvv");
                                                    usr.update(cursedata);

                                                    Check = 1;
                                                }
                                            }
                                       });

                            }
                            else{
                                Toast.makeText(EnrollCourse.this,"You Entered Wrong Enrollment Key",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

        if(Check == 1){
            Toast.makeText(EnrollCourse.this,"You Are Enrolled In The Course",Toast.LENGTH_SHORT).show();
            Check = 0;
        }
    }
}
