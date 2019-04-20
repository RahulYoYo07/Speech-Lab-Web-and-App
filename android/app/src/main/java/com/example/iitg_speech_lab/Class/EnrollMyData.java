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
    public static  ArrayList<String> courseInfoList = new ArrayList<String>();

    public static void loadCourses(String username, final TaskCompletionSource<Integer> taskda){

        coursesIDList.clear();
        coursesNameList.clear();
        coursesProfList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        

    }



}


