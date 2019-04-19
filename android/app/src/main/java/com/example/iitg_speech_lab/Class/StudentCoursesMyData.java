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
import java.util.Map;

public class StudentCoursesMyData {

    public static ArrayList<String> coursesIDList = new ArrayList<>();
    public static ArrayList<String> coursesNameList = new ArrayList<>();
    public static ArrayList<String> coursesInfoList = new ArrayList<String>();

    public static void loadCourses(String username, final TaskCompletionSource<Integer> taskda){

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
                            if (user != null) {
                                if(user.exists()){

                                    final List<Map<String,DocumentReference>> coursesRef = (List<Map<String,DocumentReference>>) user.get("CourseList");

                                    if (coursesRef != null) {
                                        for ( Map<String,DocumentReference> cRefMap : coursesRef ){
                                            DocumentReference cRef = cRefMap.get("CourseID");
                                            cRef.get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                                            if(task1.isSuccessful()){
                                                                DocumentSnapshot course = task1.getResult();
                                                                if (course != null && course.exists()) {
                                                                    coursesIDList.add(course.getString("CourseID"));
                                                                    coursesNameList.add(course.getString("CourseName"));
                                                                    coursesInfoList.add(course.getString("CourseInfo"));
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


