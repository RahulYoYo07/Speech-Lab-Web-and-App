package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.example.iitg_speech_lab.Classes.CoursesMyData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;


public class CoursesActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CoursesDataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    public TaskCompletionSource<Integer> task1;
    public Task task2;
    private Task<Void> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String username = "pradip";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new Courses", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myOnClickListener = new MyOnClickListener();

        recyclerView = ( RecyclerView ) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        Log.d("yo",username);
        CoursesMyData.loadData(username,task1);

        allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<CoursesDataModel>();
                Log.d("yo",Integer.toString(CoursesMyData.coursesIDList.size()));
                for (int i = 0; i < CoursesMyData.coursesIDList.size(); i++) {
                    data.add(new CoursesDataModel(
                            CoursesMyData.coursesIDList.get(i),
                            CoursesMyData.coursesNameList.get(i),
                            CoursesMyData.coursesInfoList.get(i)
                    ));
                }

                removedItems = new ArrayList<Integer>();

                adapter = new CustomAdapter(data);
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
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            String selectedName = ( String ) textViewName.getText();
            //int selectedItemId = -1;

//            for (int i = 0; i < CoursesMyData.coursesIDList.size(); i++) {
//                if (selectedName.equals(CoursesMyData.coursesIDList.get(i))) {
//                    //selectedItemId = CoursesMyData.coursesInfoList.get(i);
//                }
//            }
//            removedItems.add(selectedItemId);
//            data.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);

            Log.d("aman",selectedName);
            String cinfo = (String) viewHolder.itemView.getTag();
            Log.d("aman",cinfo);
            Intent intent = new Intent(CoursesActivity.this, ViewCourse.class);
            intent.putExtra("courseInfo",cinfo);
            startActivity(intent);
        }
    }



}
