package com.example.iitg_speech_lab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class VIewGroupDetails extends AppCompatActivity {

    static String groupID;
    static String assignmentID;
    static String courseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_details);

        groupID = getIntent().getStringExtra("gid");
        assignmentID = getIntent().getStringExtra("assignID");
        courseInfo = getIntent().getStringExtra("courseInfo");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference grpRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(assignmentID).collection("Groups").document(groupID);
        grpRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot grp = task.getResult();
                            if(grp.exists()){

                                TextView textviewGID = findViewById(R.id.textView9);
                                TextView textviewProjectTitle = findViewById(R.id.textView15);
                                TextView textviewDesciption = findViewById(R.id.textView16);
                                final TextView textTeam = findViewById(R.id.textView17);

                                textviewGID.setText("Group ID: " + groupID);
                                textviewDesciption.setText(grp.getString("ProblemStatement"));

                                textviewProjectTitle.setText(grp.getString("ProjectTitle"));

                                ArrayList<Map<String,Object>> members = new ArrayList<Map<String,Object>>();
                                members = (ArrayList<Map<String,Object>>) grp.get("StudentList");
                                for (Map<String,Object> member : members) {
                                    DocumentReference memRef = (DocumentReference)member.get("StudentID");
                                    memRef.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                    if (task2.isSuccessful()) {
                                                        DocumentSnapshot User = task2.getResult();
                                                        textTeam.append((User.getString("FullName")).toUpperCase() + "\n" + User.getString("RollNumber")+"\n\n");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });

    }


    public void addAttendance(View view){
        Intent intent = new Intent(VIewGroupDetails.this, UpdateAttendance.class);
        intent.putExtra("courseInfo",courseInfo);
        intent.putExtra("assignmentID",assignmentID);
        intent.putExtra("groupID",groupID);
        startActivity(intent);
    }

    public void addGrade(View view){
        Intent intent = new Intent(VIewGroupDetails.this, AddGrade.class);
        intent.putExtra("courseInfo",courseInfo);
        intent.putExtra("assignmentID",assignmentID);
        intent.putExtra("groupID",groupID);
        startActivity(intent);
    }
}
