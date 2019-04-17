package com.example.iitg_speech_lab.Class;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class GroupsMyData {

    public static ArrayList<String> assignmentsDeadlineList = new ArrayList<String>();
    public static ArrayList<String> assignmentsNameList = new ArrayList<String>();
    public static ArrayList<String> assignmentsInfoList = new ArrayList<String>();

    public static int loadAssignments(String courseInfo, final TaskCompletionSource<Integer> taskda){

        assignmentsDeadlineList.clear();
        assignmentsNameList.clear();
        assignmentsInfoList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference assignsRef = db.collection("Courses").document(courseInfo).collection("Assignments");
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot assign : task.getResult()) {
                                assignmentsInfoList.add(assign.getString("AssignmentID"));
                                assignmentsNameList.add(assign.getString("Name"));
                                assignmentsDeadlineList.add(assign.getString("About"));
                            }
                            taskda.setResult(1);
                        }
                    }
                });
        return 1;
    }
}
