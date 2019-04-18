package com.example.iitg_speech_lab.Classes;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class StudyMaterialMyData {

    public static ArrayList<String> studyMaterialsNameList = new ArrayList<String>();
    public static ArrayList<String> studyMaterialsUrlList = new ArrayList<String>();

    public static int loadStudyMaterials(String courseInfo, final TaskCompletionSource<Integer> taskda){

        studyMaterialsUrlList.clear();
        studyMaterialsNameList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference courseRef = db.collection("Courses").document(courseInfo);
        courseRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot course = task.getResult();
                            ArrayList<Map<String,String>> cms = new ArrayList<Map<String,String>>();

                            try {
                                cms = (ArrayList<Map<String, String>>) course.get("CourseMaterial");
                                for (Map<String, String> cm : cms) {
                                    studyMaterialsNameList.add(cm.get("Name"));
                                    studyMaterialsUrlList.add(cm.get("Url"));
                                }
                                taskda.setResult(1);
                            }
                            catch (Exception e) {
                                taskda.setResult(1);
                            }
                        }
                    }
                });
        return 1;
    }
}