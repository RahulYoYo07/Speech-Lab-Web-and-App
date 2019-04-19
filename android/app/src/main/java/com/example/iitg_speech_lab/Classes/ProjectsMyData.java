package com.example.iitg_speech_lab.Classes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.iitg_speech_lab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectsMyData {

    public static ArrayList<String> projectsIDList = new ArrayList<>();
    public static ArrayList<String> projectsNameList = new ArrayList<>();
    public static ArrayList<String> projectsInfoList = new ArrayList<String>();

    public static void loadProjects( final TaskCompletionSource<Integer> taskda, final String username){
        Log.d("tush",username+"fyghgcg");
        projectsIDList.clear();
        projectsNameList.clear();
        projectsInfoList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projRef;

        if (username.equals("")){
            projRef = db.collection("Projects");
            projRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    projectsIDList.add(document.getId());
                                    projectsNameList.add(document.getString("Title"));
                                    projectsInfoList.add(document.getString("Mentor"));

                                    if(projectsIDList.size()==task.getResult().size()){
                                        taskda.setResult(1);
                                    }
                                }
                            }
                        }
                    });
        }
        else {
            final DocumentReference userRef = db.collection("Users").document(username);
            db.collection("Projects")
                    .whereEqualTo("Creator",userRef)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

//                                final Integer ctr = task.getResult().size();

                                for (final QueryDocumentSnapshot document : task.getResult()) {
//                                    if((DocumentReference)document.get("Creator")==userRef){
                                        projectsIDList.add(document.getId());
                                        projectsNameList.add(document.getString("Title"));
                                        projectsInfoList.add(document.getString("Mentor"));
//                                    }

                                    if(projectsIDList.size()==task.getResult().size()){
                                        taskda.setResult(1);
                                    }
                                }
                            }
                        }
                    });
        }
    }
}