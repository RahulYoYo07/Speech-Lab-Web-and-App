package com.example.iitg_speech_lab;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddAssignment extends AppCompatActivity {
    //a constant to track the file chooser intent
    private static final String TAG = "AddAssignmentActivity";
    public String gid = "AS$$";
    String cinfo ;
    String durl = "";
    String selDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cinfo = getIntent().getStringExtra("courseInfo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        //String userName = "pradip";
        CalendarView calender = findViewById(R.id.calendarView);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                selDate = dayOfMonth + "/" + (month + 1) + "/" + year;

            }
        });
    }

    public void addAssgn(View view) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (checkDataEntered() == false)
            return;

        db.collection("Courses").document(cinfo).collection("Assignments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            gid = "AS" + Integer.toString(task.getResult().size() + 1);
                            up();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private boolean checkDataEntered() {
        EditText About = findViewById(R.id.About);
        EditText Title = findViewById(R.id.Title);
        if (Title.getText().toString().matches("")) {
            Toast.makeText(this, "Please Enter the name of the assignment", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (About.getText().toString().matches("")) {
            Toast.makeText(this, "Please Enter Short description of the assignment", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selDate.matches("")) {
            Toast.makeText(this, "Please Enter Assignment Deadline", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void up() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> newassg = new HashMap<>();
        EditText About = findViewById(R.id.About);
        EditText Title = findViewById(R.id.Title);

        newassg.put("About", About.getText().toString());
        newassg.put("Name", Title.getText().toString());
        newassg.put("AssignmentID", gid);
        newassg.put("Deadline", selDate);

        Log.d(TAG, gid);


        db.collection("Courses").document(cinfo).collection("Assignments").document(gid).set(newassg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(),"Assignment Added Successfully",Toast.LENGTH_SHORT).show();
                        AddAssignment.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
