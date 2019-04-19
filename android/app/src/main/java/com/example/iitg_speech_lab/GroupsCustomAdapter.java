package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.GroupsDataModel;

import java.util.ArrayList;

public class GroupsCustomAdapter extends RecyclerView.Adapter<GroupsCustomAdapter.MyViewHolder>  {
    private ArrayList<GroupsDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }
    }



    GroupsCustomAdapter(ArrayList<GroupsDataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public GroupsCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(FragmentGroups.myOnClickListener);

        return new GroupsCustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final GroupsCustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;
        textViewCourseID.setText(dataSet.get(listPosition).getGID());
        textViewCourseName.setText(dataSet.get(listPosition).getExtra());
        holder.itemView.setTag(dataSet.get(listPosition).getGID());

    }

    public int getItemCount() {
        return dataSet.size();
    }
}
