package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class EventDeadlines extends AppCompatActivity {
    public List<HashMap> CourseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_deadlines);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String username="gulat170123030";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        CourseUser= (List<HashMap>)document.get("CourseList");
                        try {

                            System.out.println(CourseUser.getClass().getName());
                            TextView events = findViewById(R.id.eventDeadline);
                            events.setText("");
                            System.out.println("Bakchodi");

                            for (int i=0; i<CourseUser.size(); i++){

                                CollectionReference docRef2 = ((DocumentReference)CourseUser.get(i).get("CourseID")).collection("Assignments");
                                docRef2.get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    TextView events2 = findViewById(R.id.eventDeadline);
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        try {
                                                            String asgName = document.getString("Name");
                                                            String asgAbt = document.getString("About");
                                                            String asgDeadline = document.getString("Deadline");
                                                            String sourceString = "<h2><font color=#990026>" + asgName + "</font></h2> " + "<p><font color=#007fcc>" + asgAbt + "</font></p>" + "<p><font color=#007fcc>" + asgDeadline + "</font></p>";
                                                            events2.append(Html.fromHtml(sourceString));

                                                        }
                                                        catch(Exception e){
                                                            System.out.println(e.getMessage());
                                                        }

                                                    }
                                                } else {
                                                }
                                            }
                                        });
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                    } else {

                        return;
                    }
                } else {
                    return;
                }
            }
        });
//

//
//
    }
}