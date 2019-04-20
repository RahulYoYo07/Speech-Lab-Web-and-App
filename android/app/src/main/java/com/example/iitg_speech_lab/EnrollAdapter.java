package com.example.iitg_speech_lab;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.EnrollDataModel;

import java.util.ArrayList;

public class EnrollAdapter extends RecyclerView.Adapter<EnrollAdapter.MyViewHolder> {

    private ArrayList<EnrollDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;
        TextView textViewProfName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
            this.textViewProfName = itemView.findViewById(R.id.textViewProfName);
        }
    }

    EnrollAdapter(ArrayList<EnrollDataModel> data) {
        this.dataSet = data;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_enroll_layout, parent, false);

        view.setOnClickListener(EnrollStudentCourse.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;
        TextView textViewProfName = holder.textViewProfName;

        textViewCourseID.setText(dataSet.get(listPosition).getCourseId());
        textViewCourseName.setText(dataSet.get(listPosition).getCourseName());
        textViewProfName.setText(dataSet.get(listPosition).getProfName());

        holder.itemView.setTag(dataSet.get(listPosition).getCourseId());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
