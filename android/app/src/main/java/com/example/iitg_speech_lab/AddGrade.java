package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

public class AddGrade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);
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
                                final Long Gradey = (Long) mp.get("Grade");
                                DocumentReference studref = (DocumentReference) mp.get("StudentID");
                                studref.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    LinearLayout myLayout = (LinearLayout) findViewById(R.id.myLayout);
                                                    DocumentSnapshot usr = task.getResult();
                                                    String UserName = (String) usr.get("Username");
                                                    String FullName = (String) usr.get("FullName");
                                                    TextView tv = new TextView(AddGrade.this);
                                                    EditText et = new EditText(AddGrade.this);
//                                                    LinearLayout.LayoutParams myfunnyparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    tv.setText(FullName);
                                                    tv.setTag(UserName);
                                                    et.setTag(UserName);
                                                    et.setText(Long.toString(Gradey));
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
}
