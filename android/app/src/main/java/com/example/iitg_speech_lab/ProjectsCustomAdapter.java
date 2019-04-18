package com.example.iitg_speech_lab;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.ProjectsDataModel;

import java.util.ArrayList;

public class ProjectsCustomAdapter extends RecyclerView.Adapter<ProjectsCustomAdapter.MyViewHolder> {

    private ArrayList<ProjectsDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProjectID;
        TextView textViewProjectName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewProjectID = itemView.findViewById(R.id.textViewProjectID);
            this.textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
        }
    }

    ProjectsCustomAdapter(ArrayList<ProjectsDataModel> data) {
        this.dataSet = data;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_cards_layout, parent, false);

        view.setOnClickListener(ProjectsActivity.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewProjectID = holder.textViewProjectID;
        TextView textViewProjectName = holder.textViewProjectName;

        textViewProjectID.setText(dataSet.get(listPosition).getName());
        textViewProjectName.setText(dataSet.get(listPosition).getInfo());

        holder.itemView.setTag(dataSet.get(listPosition).getId());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
