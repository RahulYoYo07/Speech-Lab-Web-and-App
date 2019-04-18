package com.example.iitg_speech_lab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class VIewGroupDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_details);

        final String groupID = getIntent().getStringExtra("gid");
        String assignmentID = getIntent().getStringExtra("assignID");
        String courseInfo = getIntent().getStringExtra("courseInfo");


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

                                textviewGID.setText(groupID);
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
                                                        textTeam.append("\n " + User.getString("FullName") + " " + User.getString("RollNumber"));
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });

    }
}
