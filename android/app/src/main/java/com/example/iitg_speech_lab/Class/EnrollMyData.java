package com.example.iitg_speech_lab.Class;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EnrollMyData {

    public static ArrayList<String> coursesIDList = new ArrayList<>();
    public static ArrayList<String> coursesNameList = new ArrayList<>();
    public static ArrayList<String> coursesProfList = new ArrayList<String>();

    public static void loadCourses(String username, final TaskCompletionSource<Integer> taskda){

        coursesIDList.clear();
        coursesNameList.clear();
        coursesProfList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(username);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if( task.isSuccessful()){

                                                   DocumentSnapshot user = task.getResult();
                                                   if (user != null) {
                                                       if(user.exists()){

                                                           final List<DocumentReference> coursesRef = (List<DocumentReference>) user.get("ProfCourseList");

                                                           if (coursesRef != null) {
                                                               for ( DocumentReference cRef : coursesRef ){
                                                                   cRef.get()
                                                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                                                                   if(task1.isSuccessful()){
                                                                                       DocumentSnapshot course = task1.getResult();
                                                                                       if (course != null && course.exists()) {
                                                                                           coursesIDList.add(course.getString("CourseID"));
                                                                                           coursesNameList.add(course.getString("CourseName"));
                                                                                           coursesProfList.add(course.getString("CourseInfo"));
                                                                                           //
                                                                                           if (coursesRef.size() == coursesIDList.size()) {
                                                                                               taskda.setResult(1);
                                                                                           }
                                                                                       }
                                                                                   }

                                                                               }
                                                                           });

                                                               }
                                                           }

                                                       }
                                                       else
                                                       {

                                                       }
                                                   }
                                               }
                                               else
                                               {

                                               }
                                           }
                                       }
                );

    }



}


