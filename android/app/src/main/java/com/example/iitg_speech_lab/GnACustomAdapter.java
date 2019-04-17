package com.example.iitg_speech_lab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iitg_speech_lab.Model.GnADataModel;

import java.util.ArrayList;

public class GnACustomAdapter extends RecyclerView.Adapter<GnACustomAdapter.MyViewHolder>  {
    private ArrayList<GnADataModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseID;
        TextView textViewCourseName;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewCourseID = itemView.findViewById(R.id.textViewCourseID);
            this.textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }
    }

    GnACustomAdapter(ArrayList<GnADataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public GnACustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(FragmentGnA.myOnClickListener);

        return new GnACustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final GnACustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewCourseID = holder.textViewCourseID;
        TextView textViewCourseName = holder.textViewCourseName;

        textViewCourseID.setText(dataSet.get(listPosition).getName());
        textViewCourseName.setText(dataSet.get(listPosition).getInfo());

        holder.itemView.setTag(dataSet.get(listPosition).getInfo());
    }

    public int getItemCount() {
        return dataSet.size();
    }
}
