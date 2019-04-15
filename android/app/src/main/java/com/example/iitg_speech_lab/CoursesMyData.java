package com.example.iitg_speech_lab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CoursesMyData extends AppCompatActivity {

    static String[] courseIDArray = {"CS201","CS333"};
    static String[] courseNameArray = {"CS","DM"};
    static Integer[] id_ = {0,1};

    public static void loadData(String username){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(username);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if( task.isSuccessful()){
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){
                                List<DocumentReference> coursesRef = (List<DocumentReference>) user.get("ProfCourseList");
                                for ( DocumentReference cRef : coursesRef ){
                                    cRef.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                                    if(task1.isSuccessful()){
                                                        DocumentSnapshot course = task1.getResult();
                                                        if(course.exists())
                                                        {
//                                                            courseIDArray.
                                                        }
                                                    }

                                                }
                                            });
                                }
                            }
                            else
                            {

                            }
                        }
                        else
                        {

                        }
                    }
                });
    }



}


