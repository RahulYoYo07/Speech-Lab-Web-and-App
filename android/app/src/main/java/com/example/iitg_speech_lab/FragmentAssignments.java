package com.example.iitg_speech_lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.AssignmentsCustomAdapter;
import com.example.iitg_speech_lab.Classes.AssignmentsMyData;
import com.example.iitg_speech_lab.Model.AssignmentsDataModel;
import com.example.iitg_speech_lab.R;
import com.example.iitg_speech_lab.ViewAssignment;
import com.example.iitg_speech_lab.ViewCourse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;

public class FragmentAssignments extends Fragment {
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<AssignmentsDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;
    private Task<Void> allTask;
    View V;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignments_layout, container, false);
        Log.d("yomanas","onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.V = view;
        String courseInfo = ViewCourse.courseInfo;

        Log.d("yomanas", "onViewCreated");
        myOnClickListener = new MyOnClickListener();

        recyclerView = view.findViewById(R.id.assignments_recycler_view);
        Log.d("yomanas","recyclerview find by id");
        recyclerView.setHasFixedSize(true);
        Log.d("yomanas","recyclerview has fixed size set");

        Log.d("yomanas","before getContext");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        //Log.d("yomanas","after getContext");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.d("yomanas","recyclerview Layout and Animator set");

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        Log.d("yomanas","about to LoadData");
        AssignmentsMyData.loadAssignments(courseInfo,task1);

        allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(AssignmentsMyData.assignmentsInfoList.size()));
                for (int i = 0; i < AssignmentsMyData.assignmentsInfoList.size(); i++) {
                    data.add(new AssignmentsDataModel(
                            AssignmentsMyData.assignmentsInfoList.get(i),
                            AssignmentsMyData.assignmentsNameList.get(i),
                            AssignmentsMyData.assignmentsDeadlineList.get(i)
                    ));
                }

                adapter = new AssignmentsCustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            viewAssignment(v);
        }

        private void viewAssignment(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewName
                    = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            String selectedName = ( String ) textViewName.getText();

            Log.d("aman",selectedName);
            String ainfo = (String) viewHolder.itemView.getTag();
            Log.d("aman",ainfo);

            Intent intent = new Intent(getActivity(), ViewAssignment.class);

            intent.putExtra("courseInfo",ViewCourse.courseInfo);
            intent.putExtra("assignID",ainfo);
            startActivity(intent);
        }
    }
}

