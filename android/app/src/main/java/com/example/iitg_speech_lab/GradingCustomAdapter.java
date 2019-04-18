package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.GradingDataModel;
import com.example.iitg_speech_lab.Model.GradingDataModel;

import java.util.ArrayList;

public class GradingCustomAdapter extends RecyclerView.Adapter<GradingCustomAdapter.MyViewHolder>  {
    private ArrayList<GradingDataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }
    }

    GradingCustomAdapter(ArrayList<GradingDataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public GradingCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(FragmentGrading.myOnClickListener);

        return new GradingCustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final GradingCustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;

        textViewCourseID.setText(dataSet.get(listPosition).getName());
        textViewCourseName.setText(dataSet.get(listPosition).getName());

        holder.itemView.setTag(dataSet.get(listPosition).getName());
    }

    public int getItemCount() {
        return dataSet.size();
    }
}
