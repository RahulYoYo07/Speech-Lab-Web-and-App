package com.example.iitg_speech_lab.Class;

import android.support.annotation.NonNull;
import android.util.Log;

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
import java.util.Map;
import java.util.Objects;


public class GnAMyData {

    public static ArrayList<Long> gnaMarksList = new ArrayList<>();
    public static ArrayList<Long> gnaAttendance = new ArrayList<>();
    public static ArrayList<String> gnaNameList = new ArrayList<>();
    public static ArrayList<String> gnaRollNo = new ArrayList<>();

    public static void loadGnA(String courseInfo ,final TaskCompletionSource<Integer> taskda){

        gnaMarksList.clear();
        gnaAttendance.clear();
        gnaNameList.clear();
        gnaRollNo.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference courseRef = db.collection("Courses").document(courseInfo);
        courseRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot course = task.getResult();
                            ArrayList<Map<String,Object>> gradelist = new ArrayList<Map<String,Object>>();
                            ArrayList<Map<String,Object>> attendancelist = new ArrayList<Map<String,Object>>();
                            gradelist = (ArrayList<Map<String, Object>>) course.get("StudentList");
                            attendancelist = (ArrayList<Map<String, Object>>) course.get("AttendanceList");


                            Log.d("gradelist",Integer.toString(gradelist.size()));

                            for (Map<String, Object> atten : attendancelist) {
                                gnaAttendance.add((long)atten.get("TotalAttendance"));
                            }

                            for (Map<String, Object> grade : gradelist) {
                                gnaMarksList.add((long) grade.get("Grade"));
                                DocumentReference studref = (DocumentReference)grade.get("StudentID");
                                studref.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot stud = task.getResult();
                                                    gnaNameList.add(stud.getString("FullName"));
                                                    gnaRollNo.add(stud.getString("RollNumber"));

                                                    if(gnaAttendance.size()==gnaRollNo.size()){
                                                        taskda.setResult(1);
                                                    }
                                                }


                                            }
                                        });



                            }



                        }
                    }
                });

    }
}
