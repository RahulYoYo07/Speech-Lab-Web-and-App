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
import java.util.Objects;


public class TAMyData {

    public static ArrayList<String> TANameList = new ArrayList<String>();
    public static ArrayList<String> TAProgramList = new ArrayList<String>();
    public static ArrayList<String> TAIDList = new ArrayList<String>();

    public static void loadTAs(String courseInfo, final TaskCompletionSource<Integer> taskda){

        TANameList.clear();
        TAProgramList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference assignsRef = db.collection("Courses").document(courseInfo);
        assignsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try {
                            if (task.isSuccessful()) {
                                DocumentSnapshot course = task.getResult();
                                ArrayList<DocumentReference> TA = new ArrayList<DocumentReference>();
                                TA = (ArrayList<DocumentReference>) course.get("TAList");
                                final Integer Counter = TA.size();
                                for (DocumentReference ta : TA) {
                                    ta.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot User = task.getResult();
                                                        TANameList.add((String) User.get("FullName"));
                                                        TAProgramList.add((String) User.get("Program"));
                                                        TAIDList.add((String) User.get("Username"));
                                                        if (TAIDList.size() == Counter) {
                                                            taskda.setResult(1);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        catch (Exception e){
                            taskda.setResult(1);
                        }
                    }
                });
    }
}
