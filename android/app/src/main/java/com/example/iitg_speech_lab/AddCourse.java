package com.example.iitg_speech_lab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

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
                String CStartYear = StartYear.getText().toString();
                String CStartType = StartSemType.getSelectedItem().toString();
                String CEndYear = EndYear.getText().toString();
                String CEndType = EndSemType.getSelectedItem().toString();
                Integer Weight = Integer.parseInt(Weighted.getText().toString());
                Courses Course = new Courses(
                        CAbout,
                        Cname,
                        Cid,
                        CEnroll,
                        CStartYear,
                        CStartType,
                        CEndYear,
                        CEndType,
                        Weight
                );
                ref.document(Cid).set(Course);

            }
        });
    }

}
