package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.StudyMaterialDataModel;

import java.util.ArrayList;

public class StudentStudyMaterialCustomAdapter extends RecyclerView.Adapter<StudentStudyMaterialCustomAdapter.MyViewHolder>  {
    private ArrayList<StudyMaterialDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }
    }

    StudentStudyMaterialCustomAdapter(ArrayList<StudyMaterialDataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public StudentStudyMaterialCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(StudentViewCourse.CMFragment.myOnClickListener);

        return new StudentStudyMaterialCustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final StudentStudyMaterialCustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;

        textViewCourseID.setText(dataSet.get(listPosition).getName());
        String url = "<a href='" + dataSet.get(listPosition).getUrl() + "'>Download<a>";

        textViewCourseName.setText(Html.fromHtml(url));

        holder.itemView.setTag(dataSet.get(listPosition).getUrl());
    }

    public int getItemCount() {
        return dataSet.size();
    }
}