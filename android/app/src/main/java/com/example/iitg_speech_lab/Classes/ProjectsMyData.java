package com.example.iitg_speech_lab.Classes;

import android.support.annotation.NonNull;

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
import java.util.List;

public class ProjectsMyData {

    public static ArrayList<String> projectsIDList = new ArrayList<>();
    public static ArrayList<String> projectsNameList = new ArrayList<>();
    public static ArrayList<String> projectsInfoList = new ArrayList<String>();

    public static void loadProjects( final TaskCompletionSource<Integer> taskda){

        projectsIDList.clear();
        projectsNameList.clear();
        projectsInfoList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projRef = db.collection("Projects");
        projRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if( task.isSuccessful()){
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                    projectsIDList.add(document.getId());
                                    projectsNameList.add(document.getString("Title"));
                                    projectsInfoList.add(document.getString("Mentor"));
                            }
                            taskda.setResult(1);
                        }

                    }
                });
    }
}