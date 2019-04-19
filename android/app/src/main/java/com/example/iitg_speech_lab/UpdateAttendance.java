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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class UpdateAttendance extends AppCompatActivity {

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
                            Integer Count = 0;
                            for (Map<Object, Object> mp : arr) {
                                Count++;
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
//                                                    LinearLayout.LayoutParams myfunnyparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    tv.setText(FullName);
                                                    tv.setTag(UserName);
                                                    et.setTag(UserName);
                                                    myLayout.addView(tv);
                                                    myLayout.addView(et);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    public void Update_Attendance_Group(View view){
        String CourseInfo = getIntent().getStringExtra("courseInfo");
        String AssignmentID = getIntent().getStringExtra("assignmentID");
        String GroupID = getIntent().getStringExtra("groupID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference assignsRef = db.collection("Courses").document(CourseInfo);
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        
                    }
                });

    }

}
