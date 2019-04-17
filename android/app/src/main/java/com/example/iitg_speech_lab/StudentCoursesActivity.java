package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.example.iitg_speech_lab.Class.StudentCoursesMyData;
import com.example.iitg_speech_lab.Model.StudentCoursesDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;


public class StudentCoursesActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static ArrayList<StudentCoursesDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String username = "pradip";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentCoursesActivity.this, AddCourse.class);
                startActivity(intent);
            }
        });

        myOnClickListener = new MyOnClickListener();


        recyclerView = findViewById(R.id.student_courses_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        Log.d("yo",username);
        StudentCoursesMyData.loadCourses(username,task1);

        Task<Void> allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(StudentCoursesMyData.coursesIDList.size()));
                for (int i = 0; i < StudentCoursesMyData.coursesIDList.size(); i++) {
                    data.add(new StudentCoursesDataModel(
                            StudentCoursesMyData.coursesIDList.get(i),
                            StudentCoursesMyData.coursesNameList.get(i),
                            StudentCoursesMyData.coursesInfoList.get(i)
                    ));
                }

                adapter = new StudentCoursesCustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            viewStudentCourse(v);
        }

        private void viewStudentCourse(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = null;
            if (viewHolder != null) {
                textViewName = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            }
            String selectedName = ( String ) textViewName.getText();

            Log.d("aman",selectedName);
            String cinfo = null;
            if (viewHolder != null) {
                cinfo = (String) viewHolder.itemView.getTag();
            }
            Log.d("aman",cinfo);
            Intent intent = new Intent(StudentCoursesActivity.this, ViewCourse.class);
            intent.putExtra("courseInfo",cinfo);
            startActivity(intent);
        }
    }
}
