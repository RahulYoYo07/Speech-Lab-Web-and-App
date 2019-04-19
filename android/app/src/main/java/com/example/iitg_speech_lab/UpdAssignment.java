package com.example.iitg_speech_lab;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdAssignment extends AppCompatActivity {
    //a constant to track the file chooser intent
    private static final int PICK_FILE_REQUEST = 234;
    private static final String TAG = "AddAssignmentActivity";
    public String gid = "AS$$";
    String cinfo = "HS241_pradip_2019";
    String durl = "";
    String selDate = "";
    //a Uri object to store file path
    private Uri filePath;
//    String aid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        String userName = "pradip";
        final CalendarView calender = findViewById(R.id.calendarView);
        final EditText About = findViewById(R.id.About);
        final EditText Title = findViewById(R.id.Title);

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

        up();

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
