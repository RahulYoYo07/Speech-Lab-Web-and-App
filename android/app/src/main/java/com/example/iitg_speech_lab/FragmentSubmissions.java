package com.example.iitg_speech_lab;

import android.content.Intent;
import android.net.Uri;
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

import com.example.iitg_speech_lab.Class.SubmissionsMyData;
import com.example.iitg_speech_lab.Model.SubmissionsDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;

public class FragmentSubmissions extends Fragment {
    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static ArrayList<SubmissionsDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;
    View V;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submissions, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.V = view;
        String courseInfo = ViewAssignment.courseInfo;
        String assignmentID = ViewAssignment.assignmentID;

        myOnClickListener = new MyOnClickListener();

        recyclerView = view.findViewById(R.id.submissions_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        SubmissionsMyData.loadSubmissions(courseInfo,assignmentID,task1);

        Task<Void> allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(SubmissionsMyData.submissionNameList.size()));
                for (int i = 0; i < SubmissionsMyData.submissionNameList.size(); i++) {
                    data.add(new SubmissionsDataModel(
                            SubmissionsMyData.submissionGIDList.get(i),
                            SubmissionsMyData.submissionNameList.get(i),
                            SubmissionsMyData.submisssionUrlList.get(i)
                    ));
                }

                adapter = new SubmissionsCustomAdapter(data);
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
                    = null;
            if (viewHolder != null) {
                textViewName = viewHolder.itemView.findViewById(R.id.textViewCourseID);
            }

            String url = (String) viewHolder.itemView.getTag();

            Log.d("url",url);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//
        }
    }
}
