package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.SubmissionsDataModel;

import java.util.ArrayList;

public class SubmissionsCustomAdapter extends RecyclerView.Adapter<SubmissionsCustomAdapter.MyViewHolder>  {
    private ArrayList<SubmissionsDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }
    }

    SubmissionsCustomAdapter(ArrayList<SubmissionsDataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public SubmissionsCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(FragmentSubmissions.myOnClickListener);

        return new SubmissionsCustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final SubmissionsCustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;

        textViewCourseID.setText(dataSet.get(listPosition).getGID());
        textViewCourseName.setText(dataSet.get(listPosition).getName());

        holder.itemView.setTag(dataSet.get(listPosition).getURL());
    }

    public int getItemCount() {
        return dataSet.size();
    }
}
