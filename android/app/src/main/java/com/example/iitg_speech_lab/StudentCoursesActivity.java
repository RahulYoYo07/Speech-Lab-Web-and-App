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

import com.example.iitg_speech_lab.Class.StudentCoursesAsTAMyData;
import com.example.iitg_speech_lab.Class.StudentCoursesMyData;
import com.example.iitg_speech_lab.Model.StudentCoursesAsTADataModel;
import com.example.iitg_speech_lab.Model.StudentCoursesDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;


public class StudentCoursesActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.Adapter adapter2;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private static ArrayList<StudentCoursesDataModel> data;
    private static ArrayList<StudentCoursesAsTADataModel> data2;
    static View.OnClickListener myOnClickListener;
    static View.OnClickListener myOnClickListener2;
    public TaskCompletionSource<Integer> task1;
    public TaskCompletionSource<Integer> task11;
    public Task task2;
    public Task task22;

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    //     String username = getIntent().getStringExtra("username");

    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = "gulat170123030";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_courses);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StudentCoursesActivity.this, AddCourse.class);
//                startActivity(intent);
//            }
//        });

        myOnClickListener = new MyOnClickListener();
        myOnClickListener2 = new MyOnClickListener2();

        recyclerView = findViewById(R.id.student_courses_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView2 = findViewById(R.id.student_courses_asTA_recycler_view);
        recyclerView2.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());


        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();

        task11 = new TaskCompletionSource<>();
        task22 = task11.getTask();

        Log.d("yo",username);
        StudentCoursesMyData.loadCourses(username,task1);
        StudentCoursesAsTAMyData.loadCourses(username,task11);

        Task<Void> allTask = Tasks.whenAll(task2,task22);

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

                data2 = new ArrayList<>();
                Log.d("yo",Integer.toString(StudentCoursesAsTAMyData.coursesIDList.size()));
                for (int i = 0; i < StudentCoursesAsTAMyData.coursesIDList.size(); i++) {
                    data2.add(new StudentCoursesAsTADataModel(
                            StudentCoursesAsTAMyData.coursesIDList.get(i),
                            StudentCoursesAsTAMyData.coursesNameList.get(i),
                            StudentCoursesAsTAMyData.coursesInfoList.get(i)
                    ));
                }

                adapter2 = new StudentCoursesAsTACustomAdapter(data2);
                recyclerView2.setAdapter(adapter2);
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


//            Intent intent = new Intent(StudentCoursesActivity.this, ViewCourse.class);
//            intent.putExtra("courseInfo",cinfo);
//            startActivity(intent);

            Intent intent = new Intent(StudentCoursesActivity.this, StudentViewCourse.class);
            intent.putExtra("courseInfo",cinfo);
            startActivity(intent);
        }
    }


    private class MyOnClickListener2 implements View.OnClickListener {

        private MyOnClickListener2() {
        }

        @Override
        public void onClick(View v) {
            viewStudentCourseAsTA(v);
        }

        private void viewStudentCourseAsTA(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = null;
            if (viewHolder != null) {
                textViewName = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            }
            String selectedName = ( String ) textViewName.getText();

            String cinfo = null;
            if (viewHolder != null) {
                cinfo = (String) viewHolder.itemView.getTag();
            }

            Intent intent = new Intent(StudentCoursesActivity.this, ViewCourse.class);
            intent.putExtra("courseInfo",cinfo);
            startActivity(intent);
        }
    }
}
