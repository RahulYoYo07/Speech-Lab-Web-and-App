package com.example.iitg_speech_lab.Class;

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
import java.util.Collection;
import java.util.Map;


public class GroupsMyData {

    public static ArrayList<String> GroupIdList = new ArrayList<String>();
    public static ArrayList<String> GroupExtraList = new ArrayList<String>();

    public static int loadGroups(String courseInfo,String AssignmentID, final TaskCompletionSource<Integer> taskda){

        GroupIdList.clear();
        GroupExtraList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference assignsRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(AssignmentID).collection("Groups");
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    GroupIdList.add((String) document.get("GroupID"));
                                    GroupExtraList.add("");
                                }
                                taskda.setResult(1);
                            }
                            catch (Exception e){
                                taskda.setResult(1);
                            }
                        }
                    }
                });
        return 1;
    }
}
