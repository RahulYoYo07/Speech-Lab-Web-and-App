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

//import com.example.iitg_speech_lab.Classes.StudyMaterialMyData;

import com.example.iitg_speech_lab.Class.StudyMaterialMyData;
import com.example.iitg_speech_lab.Model.StudyMaterialDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;

public class FragmentStudyMaterial extends Fragment {
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<StudyMaterialDataModel> data;
    static View.OnClickListener myOnClickListener;
    public TaskCompletionSource<Integer> task1;
    public Task task2;
    private Task<Void> allTask;
    View V;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_material, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.V = view;
        String courseInfo = ViewCourse.courseInfo;

        myOnClickListener = new MyOnClickListener();

        recyclerView = view.findViewById(R.id.study_material_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        task1 = new TaskCompletionSource<>();
        task2 = task1.getTask();
        StudyMaterialMyData.loadStudyMaterials(courseInfo,task1);

        allTask = Tasks.whenAll(task2);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data = new ArrayList<>();
                Log.d("yo",Integer.toString(StudyMaterialMyData.studyMaterialsNameList.size()));
                for (int i = 0; i < StudyMaterialMyData.studyMaterialsNameList.size(); i++) {
                    data.add(new StudyMaterialDataModel(
                            StudyMaterialMyData.studyMaterialsNameList.get(i),
                            StudyMaterialMyData.studyMaterialsUrlList.get(i)
                    ));
                }

                adapter = new StudyMaterialCustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            viewCM(v);
        }

        private void viewCM(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);

            TextView textViewName
                    = viewHolder.itemView.findViewById(R.id.textViewCourseID);

            String selectedName = ( String ) textViewName.getText();

            String cminfo = (String) viewHolder.itemView.getTag();

            Log.d("cm",cminfo);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(cminfo)));
//            Intent intent = new Intent(AssignmentsActivity.this, ViewCourse.class);
//            intent.putExtra("courseInfo",cinfo);
//            startActivity(intent);
        }
    }
}
