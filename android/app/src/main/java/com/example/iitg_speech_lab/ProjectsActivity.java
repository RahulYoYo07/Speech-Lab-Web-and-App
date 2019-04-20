package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.iitg_speech_lab.Classes.ProjectsMyData;
import com.example.iitg_speech_lab.Model.ProjectsDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;


public class ProjectsActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static ArrayList<ProjectsDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Projects");
        setSupportActionBar(toolbar);
        final String username = getIntent().getStringExtra("username");
        Log.d("Navee",username);
        myOnClickListener = new MyOnClickListener();


        recyclerView = findViewById(R.id.projects_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        ProjectsMyData.loadProjects(task1, username);

        Task<Void> allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(ProjectsMyData.projectsIDList.size()));
                for (int i = 0; i < ProjectsMyData.projectsIDList.size(); i++) {
                    data.add(new ProjectsDataModel(
                            ProjectsMyData.projectsIDList.get(i),
                            ProjectsMyData.projectsNameList.get(i),
                            ProjectsMyData.projectsInfoList.get(i)
                    ));
                }

                adapter = new ProjectsCustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            viewProject(v);
        }

        private void viewProject(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = null;
            if (viewHolder != null) {
                textViewName = viewHolder.itemView.findViewById(R.id.textViewProjectName);
            }
            String selectedName = ( String ) textViewName.getText();

            Log.d("aman",selectedName);
            String cinfo = null;
            if (viewHolder != null) {
                cinfo = (String) viewHolder.itemView.getTag();
            }
            Log.d("aman",cinfo);
            finish();
            Intent intent = new Intent(ProjectsActivity.this, ViewProject.class);
            intent.putExtra("projectID",cinfo);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        }
    }
}
