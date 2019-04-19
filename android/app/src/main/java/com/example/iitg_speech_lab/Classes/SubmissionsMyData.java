package com.example.iitg_speech_lab.Classes;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SubmissionsMyData {

    public static ArrayList<String> submissionNameList = new ArrayList<String>();
    public static ArrayList<String> submisssionUrlList = new ArrayList<String>();
    public static ArrayList<String> submissionGIDList = new ArrayList<String>();

    public static int loadSubmissions(String courseInfo,String assignmentID, final TaskCompletionSource<Integer> taskda){

        submissionNameList.clear();
        submisssionUrlList.clear();
        submissionGIDList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(assignmentID).collection("Groups");
        groupsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot group : task.getResult()) {
                                submissionGIDList.add((String) group.getId());
                                Map<String,String> submission= new HashMap<>();
                                try {
                                    submission = (Map<String,String>) group.get("SubmissionFile");
                                    submisssionUrlList.add(submission.get("Url"));
                                    submissionNameList.add(submission.get("Name"));
                                }
                                catch (Exception e){

                                }

                            }
                            taskda.setResult(1);
                        }
                    }
                });
        return 1;
    }
}
