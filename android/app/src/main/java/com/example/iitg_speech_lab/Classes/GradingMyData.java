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


public class GradingMyData {

    public static ArrayList<Long> StudentGradeList = new ArrayList<Long>();
    public static ArrayList<String> StudentNameList = new ArrayList<String>();
    public static Integer Counter = 100000;
    public static Integer Check = 0;


    public static void loadGrades(String courseInfo, String assignmentID, final TaskCompletionSource<Integer> taskda) {

        StudentGradeList.clear();
        StudentNameList.clear();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String CourseInfo = courseInfo;
        final String assID = assignmentID;
        DocumentReference docref = db.collection("Courses").document(courseInfo);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    ArrayList<Map<Object, Object>> StdList = new ArrayList<Map<Object, Object>>();
                    StdList = (ArrayList<Map<Object, Object>>) doc.get("StudentList");
                    Counter = StdList.size();
                    Log.d("yobaby",Integer.toString(Counter));
                    CollectionReference assignsRef = db.collection("Courses").document(CourseInfo).collection("Assignments").document(assID).collection("Groups");
                    assignsRef.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                            Log.d("yobaby",Integer.toString(Counter));
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String gid = ((String) document.get("GroupID"));
                                                FirebaseFirestore dd = FirebaseFirestore.getInstance();
                                                DocumentReference group = dd.collection("Courses").document(CourseInfo).collection("Assignments").document(assID).collection("Groups").document(gid);
                                                group.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {

                                                                        DocumentSnapshot gp = task.getResult();
                                                                        ArrayList<Map<Object, Object>> Stud = new ArrayList<Map<Object, Object>>();
                                                                        Stud = (ArrayList<Map<Object, Object>>) gp.get("StudentList");
                                                                        for (Map<Object, Object> mp : Stud) {
                                                                            StudentGradeList.add((Long) mp.get("Grade"));
                                                                            DocumentReference user = ((DocumentReference) mp.get("StudentID"));
                                                                            user.get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                DocumentSnapshot people = task.getResult();
                                                                                                StudentNameList.add((String) people.get("FullName"));
                                                                                                if (StudentNameList.size() == Counter) {
                                                                                                    Log.d("yobaby",Integer.toString(StudentNameList.size()));
                                                                                                    Log.d("yobaby",Integer.toString(Counter));
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
                                }
                            });
                }
            }
        });
//        if (Check == 1) {
//            StudentGradeList.add(4);
//            StudentNameList.add("Ravi");
//            taskda.setResult(1);
//            CollectionReference assignsRef = db.collection("Courses").document(courseInfo).collection("Assignments").document(assignmentID).collection("Groups");
//            assignsRef.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                try {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        String gid = ((String) document.get("GroupID"));
//                                        FirebaseFirestore dd = FirebaseFirestore.getInstance();
//                                        DocumentReference group = dd.collection("Courses").document(CourseInfo).collection("Assignments").document(assID).collection("Groups").document(gid);
//                                        group.get()
//                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                        if (task.isSuccessful()) {
//                                                            try {
//                                                                DocumentSnapshot gp = task.getResult();
//                                                                ArrayList<Map<Object, Object>> Stud = new ArrayList<Map<Object, Object>>();
//                                                                Stud = (ArrayList<Map<Object, Object>>) gp.get("StudentList");
//                                                                for (Map<Object, Object> mp : Stud) {
//                                                                    StudentGradeList.add((Integer) mp.get("Grade"));
//                                                                    Log.d("Error", "Message");
//                                                                    DocumentReference user = ((DocumentReference) mp.get("StudentID"));
//                                                                    user.get()
//                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                                @Override
//                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                                    if (task.isSuccessful()) {
//                                                                                        DocumentSnapshot people = task.getResult();
//                                                                                        StudentNameList.add((String) people.get("FullName"));
//                                                                                        if (Counter == StudentNameList.size()) {
//                                                                                            taskda.setResult(1);
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                            });
//                                                                }
//                                                            } catch (Exception e) {
//                                                                taskda.setResult(1);
//                                                            }
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                } catch (Exception e) {
//                                    taskda.setResult(1);
//                                }
//                            }
//                        }
//                    });
//        }
    }
}