package com.example.iitg_speech_lab.Class;

import android.support.annotation.NonNull;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrollMyData {

    public static ArrayList<String> coursesIDList = new ArrayList<>();
    public static ArrayList<String> coursesNameList = new ArrayList<>();
    public static ArrayList<String> coursesProfList = new ArrayList<String>();
    public static  ArrayList<String> courseInfoList = new ArrayList<String>();
    public static void loadCourses(String Username, final TaskCompletionSource<Integer> taskda){

        coursesIDList.clear();
        coursesNameList.clear();
        coursesProfList.clear();
        courseInfoList.clear();
        final String username = Username;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference courses = db.collection("Courses");
        courses.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            final int count = task.getResult().size();
                            coursesIDList.add(document.getString("CourseID"));
                            courseInfoList.add(document.getString("CourseInfo"));
                            coursesNameList.add(document.getString("CourseName"));
                            ArrayList<DocumentReference> profRef = (ArrayList<DocumentReference>)document.get("FacultyList");
                            DocumentReference prof = profRef.get(0);
                            prof.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot profDet = task.getResult();
                                            coursesProfList.add(profDet.getString("FullName"));

                                            if(coursesProfList.size()==count){
                                                taskda.setResult(1);
                                            }
                                        }
                                    });
                        }

                    }
                });
    }
}


