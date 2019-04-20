package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ViewMyGroup extends AppCompatActivity {

    static String assignmentID;
    static String courseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_group);

        final String username = StudentCoursesActivity.username;
        assignmentID = getIntent().getStringExtra("assignID");
        courseInfo = getIntent().getStringExtra("courseInfo");
        Log.d("kkk",username);
        Log.d("kkk",assignmentID);
        Log.d("kkk",courseInfo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("Users").document(username);
        CollectionReference grpsRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(assignmentID).collection("Groups");
        grpsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot grp : task.getResult()){
                                ArrayList<Map<String,Object>> mp = (ArrayList<Map<String,Object>>)grp.get("StudentList");
                                for(Map<String,Object> x : mp ){
                                    if(x.get("StudentID").equals(userRef)){
                                        String groupID = grp.getId();
                                        TextView textviewGID = findViewById(R.id.textView100);
                                        TextView textviewProjectTitle = findViewById(R.id.textView101);
                                        TextView textviewDesciption = findViewById(R.id.textView102);
                                        final TextView textTeam = findViewById(R.id.textView103);


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
                        }
                    }
                });
    }
}
