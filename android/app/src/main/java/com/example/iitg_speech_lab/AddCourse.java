package com.example.iitg_speech_lab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class AddCourse extends AppCompatActivity {
    EditText CourseID;
    EditText CourseName;
    EditText StartYear;
    EditText EndYear;
    EditText AboutCourse;
    EditText EnrollmentKey;
    EditText Weighted;
    Spinner StartSemType;
    Spinner EndSemType;
    Button Add_Course;

    FirebaseFirestore db;
    CollectionReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        CourseName = (EditText) findViewById(R.id.CourseName);
        CourseID = (EditText) findViewById(R.id.CourseID);
        AboutCourse = (EditText) findViewById(R.id.AboutCourse);
        StartYear = (EditText) findViewById(R.id.StartYear);
        EndYear = (EditText) findViewById(R.id.EndYear);
        EnrollmentKey = (EditText) findViewById(R.id.EnrollmentKey);
        Weighted = (EditText) findViewById(R.id.Weightage);
        StartSemType = (Spinner) findViewById(R.id.StartSemType);
        EndSemType = (Spinner) findViewById(R.id.EndSemType);
        Add_Course = (Button) findViewById(R.id.Add_Course);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("Courses");

        Add_Course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cname = CourseName.getText().toString();
                String Cid = CourseID.getText().toString();
                String CAbout = AboutCourse.getText().toString();
                String CEnroll = EnrollmentKey.getText().toString();
                Integer CStartYear = Integer.parseInt(StartYear.getText().toString());
                String CStartType = StartSemType.getSelectedItem().toString();
                Integer CEndYear = Integer.parseInt(EndYear.getText().toString());
                String CEndType = EndSemType.getSelectedItem().toString();
                final Integer Weight = Integer.parseInt(Weighted.getText().toString());
                Map<String,Object> CourseMap = new HashMap<>();
                CourseMap.put("AboutCourse",CAbout);
                CourseMap.put("CourseID",Cid);
                CourseMap.put("EnrollmentKey",CEnroll);
                CourseMap.put("CourseName",Cname);
                Map<String,Object> StartSem = new HashMap<>();
                StartSem.put("SemesterType",CStartType);
                StartSem.put("Session",CStartYear);
                CourseMap.put("StartSemester",StartSem);
                Map<String,Object> EndSem = new HashMap<>();
                EndSem.put("SemesterType",CEndType);
                EndSem.put("Session",CEndYear);
                CourseMap.put("EndSemester",EndSem);
                CourseMap.put("Weightage",Weight);
                ref.document(Cid).set(CourseMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CourseName.setText("");
                        CourseID.setText("");
                        AboutCourse.setText("");
                        EnrollmentKey.setText("");
                        StartYear.setText("");
                        EndYear.setText("");
                        Weighted.setText("");
                        Toast.makeText(AddCourse.this, "Course Created", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
