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

import com.example.iitg_speech_lab.CoursesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoursesMyData {

    public static ArrayList<String> coursesIDList = new ArrayList<String>();
    public static ArrayList<String> coursesNameList = new ArrayList<String>();
    public static ArrayList<String> coursesInfoList = new ArrayList<String>();

    public static int loadData(String username, final TaskCompletionSource<Integer> taskda){

        coursesIDList.clear();
        coursesNameList.clear();
        coursesInfoList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(username);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if( task.isSuccessful()){
                            DocumentSnapshot user = task.getResult();
                            if(user.exists()){

                                final List<DocumentReference> coursesRef = (List<DocumentReference>) user.get("ProfCourseList");

                                for ( DocumentReference cRef : coursesRef ){
                                    cRef.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                                    if(task1.isSuccessful()){
                                                        DocumentSnapshot course = task1.getResult();
                                                        if(course.exists())
                                                        {
                                                            //Log.d("hi",course.getString("CourseID"));
                                                            coursesIDList.add(course.getString("CourseID"));
                                                            coursesNameList.add(course.getString("CourseName"));
                                                            coursesInfoList.add(course.getString("CourseInfo"));
//
                                                            if(coursesRef.size()==coursesIDList.size())
                                                            {
                                                                taskda.setResult(1);
                                                            }
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
                }
                );

        return 1;
    }



}


