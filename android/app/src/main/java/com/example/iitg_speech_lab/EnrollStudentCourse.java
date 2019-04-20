package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iitg_speech_lab.Class.EnrollMyData;
import com.example.iitg_speech_lab.Model.EnrollDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class EnrollStudentCourse extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static ArrayList<EnrollDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String username = StudentCoursesActivity.username;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_student_course);

        myOnClickListener = new MyOnClickListener();


        recyclerView = findViewById(R.id.student_enroll_course_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        Log.d("yo",username);
        EnrollMyData.loadCourses(username,task1);

        Task<Void> allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(EnrollMyData.coursesIDList.size()));
                for (int i = 0; i < EnrollMyData.coursesIDList.size(); i++) {
                    data.add(new EnrollDataModel(
                            EnrollMyData.coursesIDList.get(i),
                            EnrollMyData.coursesNameList.get(i),
                            EnrollMyData.coursesProfList.get(i),
                            EnrollMyData.courseInfoList.get(i)
                    ));
                }

                adapter = new EnrollAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            viewCourse(v);
        }

        private void viewCourse(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            final RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = null;
            if (viewHolder != null) {
                textViewName = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            }
            String selectedName = null;
            if (textViewName != null) {
                selectedName = ( String ) textViewName.getText();
            }

            String cinfo = null;
            if (viewHolder != null) {
                cinfo = (String) viewHolder.itemView.getTag();
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference studRef = db.collection("Users").document(StudentCoursesActivity.username);

            studRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot user = task.getResult();
                            try {
                                String courseInfo = (String)viewHolder.itemView.getTag();
                                FirebaseFirestore dc = FirebaseFirestore.getInstance();
                                DocumentReference courseRef = dc.collection("Courses").document(courseInfo);
                                ArrayList<Map<String,DocumentReference>> courses = (ArrayList<Map<String,DocumentReference>>) user.get("CourseList");
                                int f=1;
                                for(Map<String,DocumentReference>course: courses){
                                    if((course.get("CourseID")).equals(courseRef)){
                                        f=0;
                                    }
                                }

                                if(f==1){
                                    Intent intent = new Intent(EnrollStudentCourse.this, EnrollCourse.class);
                                    intent.putExtra("courseInfo",courseInfo);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(EnrollStudentCourse.this,"Already enrolled",Toast.LENGTH_LONG).show();
                                }
                            }
                            finally {

                            }
                        }
                    });


        }
    }



}
