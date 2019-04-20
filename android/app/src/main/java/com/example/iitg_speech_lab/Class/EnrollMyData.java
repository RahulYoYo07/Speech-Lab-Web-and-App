package com.example.iitg_speech_lab.Class;

import android.support.annotation.NonNull;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrollMyData {

    public static ArrayList<String> coursesIDList = new ArrayList<>();
    public static ArrayList<String> coursesNameList = new ArrayList<>();
    public static ArrayList<String> coursesProfList = new ArrayList<String>();
    public static  ArrayList<String> courseInfoList = new ArrayList<String>();
    public static Integer check = 0;
    public static Integer doublecheck = 0;
    public static final ArrayList<DocumentReference> courselist = new ArrayList<DocumentReference>();
    public static final ArrayList<DocumentReference> unregisteredlist = new ArrayList<DocumentReference>();
    public static void loadCourses(String Username, final TaskCompletionSource<Integer> taskda){

        coursesIDList.clear();
        coursesNameList.clear();
        coursesProfList.clear();
        final String username = Username;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference courses = db.collection("Courses");
        courses.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseFirestore dc = FirebaseFirestore.getInstance();
                            DocumentReference das = dc.collection("Courses").document((String) document.get("CourseInfo"));
                            courselist.add(das);
                        }
                        DocumentReference usr = db.collection("Users").document(username);
                        usr.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            int count=0;
                                            ArrayList<Map<Object, Object>> mp = new ArrayList<Map<Object, Object>>();
                                            mp = (ArrayList<Map<Object, Object>>) doc.get("CourseList");
                                            for (Map<Object,Object> mps : mp) {
                                                count++;
                                                DocumentReference docy = (DocumentReference) mps.get("CourseID");
                                                int x = 0;
                                                for (int i=0;i<courselist.size();i++) {
                                                    if (courselist.get(i).equals(docy)){
                                                        x = 1;
                                                    }
                                                }
                                                if (x == 0) {
                                                    DocumentReference dc = docy;
                                                    dc.get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    DocumentSnapshot dfg = task.getResult();
                                                                    String cid = (String) dfg.get("CourseID");
                                                                    String cname = (String) dfg.get("CourseName");
                                                                    String info = (String) dfg.get("CourseInfo");
                                                                    coursesIDList.add(cid);
                                                                    coursesNameList.add(cname);
                                                                    courseInfoList.add(info);
                                                                    ArrayList<DocumentReference> ghy = (ArrayList<DocumentReference>) dfg.get("FacultyList");
                                                                    ghy.get(0).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    DocumentSnapshot refer = task.getResult();
                                                                                    String profname = (String) refer.get("FullName");
                                                                                    coursesProfList.add(profname);
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }


                                            }
                                        }
                                    }
                                });
                    }
                });
    }
}


