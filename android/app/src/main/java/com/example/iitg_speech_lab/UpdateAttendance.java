package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateAttendance extends AppCompatActivity {
    public static ArrayList<CheckBox> TickBoxes = new ArrayList<CheckBox>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attendance);
        String CourseInfo = getIntent().getStringExtra("courseInfo");
        String AssignmentID = getIntent().getStringExtra("assignmentID");
        String GroupID = getIntent().getStringExtra("groupID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference assignsRef = db.collection("Courses").document(CourseInfo).collection("Assignments").document(AssignmentID).collection("Groups").document(GroupID);
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot group = task.getResult();
                            ArrayList<Map<Object, Object>> arr = new ArrayList<Map<Object, Object>>();
                            arr = (ArrayList<Map<Object, Object>>) group.get("StudentList");
                            final Integer Counter = arr.size();
                            for (Map<Object, Object> mp : arr) {
                                DocumentReference studref = (DocumentReference) mp.get("StudentID");
                                studref.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    LinearLayout myLayout = (LinearLayout) findViewById(R.id.mylayout);
                                                    DocumentSnapshot usr = task.getResult();
                                                    String UserName = (String) usr.get("Username");
                                                    String FullName = (String) usr.get("FullName");
                                                    TextView tv = new TextView(UpdateAttendance.this);
                                                    CheckBox et = new CheckBox(UpdateAttendance.this);
                                                    tv.setText(FullName);
                                                    tv.setTag(UserName);
                                                    et.setTag(UserName);
                                                    et.setText(FullName.toUpperCase());
                                                    et.setTextSize(18);
                                                    TickBoxes.add(et);
                                                    myLayout.addView(et);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    public static ArrayList<Map<Object,Object>> push = new ArrayList<Map<Object,Object>>();
    public static int cnt;
    public static int pp = 0;
    public void Update_Attendance_Group(final View view){

        final String CourseInfo = getIntent().getStringExtra("courseInfo");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference assignsRef = db.collection("Courses").document(CourseInfo);
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot course = task.getResult();
                            final ArrayList<Map<Object,Object>> mps = (ArrayList<Map<Object, Object>>) course.get("AttendanceList");
                            final int lengthy = mps.size();
                            cnt = 0;
                            push.clear();
                            for (Map<Object,Object>mp : mps) {
                                pp = 0;
                                final DocumentReference usr = (DocumentReference) mp.get("StudentID");
                                final Long Attendance = (Long) mp.get("TotalAttendance");

                                usr.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    cnt++;
                                                    DocumentSnapshot user = task.getResult();
                                                    String UserName = (String) user.get("Username");
                                                    int x = 0;
                                                    for (int i=0;i<TickBoxes.size();i++) {
                                                        final Map<Object,Object> mpt = new HashMap<>();
                                                        if (TickBoxes.get(i).getTag().toString().equals(UserName) && TickBoxes.get(i).isChecked()) {
                                                            Long Atd = Attendance + 1;
                                                            mpt.put("StudentID",usr);
                                                            mpt.put("TotalAttendance", Atd);
                                                            x = 1;
                                                            push.add(mpt);
                                                        }
                                                    }
                                                    if(x == 0){
                                                        for (int i=0;i<TickBoxes.size();i++) {
                                                            final Map<Object,Object> mpt = new HashMap<>();
                                                            if (TickBoxes.get(i).getTag().toString().equals(UserName)) {
                                                                Long Atd = Attendance ;
                                                                mpt.put("StudentID",usr);
                                                                mpt.put("TotalAttendance", Atd);
                                                                x = 1;
                                                                push.add(mpt);
                                                            }
                                                        }
                                                    }
                                                    if(x == 0){
                                                        final Map<Object,Object> mpt = new HashMap<>();
                                                        Long Atd = Attendance ;
                                                        mpt.put("StudentID",usr);
                                                        mpt.put("TotalAttendance", Atd);
                                                        x = 1;
                                                        push.add(mpt);
                                                    }
                                                    pp = 1;
                                                    if (cnt == lengthy) {
                                                        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                        DocumentReference assignsRef = db2.collection("Courses").document(CourseInfo);
                                                        Map<String, Object> mpdgrd = new HashMap<>();
                                                        mpdgrd.put("AttendanceList", push);
                                                        System.out.print(mpdgrd);
                                                        System.out.println("drdfsdgdgdsgsd");
                                                        assignsRef.update(mpdgrd);
                                                        pp = 1;
                                                        mpdgrd.clear();
                                                        push.clear();
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        Toast.makeText(UpdateAttendance.this,"Attendances Updated",Toast.LENGTH_LONG).show();
    }



}
