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

        TextView textViewStudentName;
        TextView textViewRollNo;
        TextView textViewAttendance;
        TextView textViewMarks;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            this.textViewRollNo = itemView.findViewById(R.id.textViewRollNo);
            this.textViewAttendance = itemView.findViewById(R.id.textViewAttendance);
            this.textViewMarks = itemView.findViewById(R.id.textViewMarks);
        }
    }

    GnACustomAdapter(ArrayList<GnADataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    public GnACustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gna_cards_layout, parent, false);

        view.setOnClickListener(FragmentGnA.myOnClickListener);

        return new GnACustomAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final GnACustomAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewStudentName = holder.textViewStudentName;
        TextView textViewRollNo = holder.textViewRollNo;
        TextView textViewAttendance = holder.textViewAttendance;
        TextView textViewMarks = holder.textViewMarks;

        textViewStudentName.setText(dataSet.get(listPosition).getName());
        textViewRollNo.setText("Roll No. : " + dataSet.get(listPosition).getRollNo());
        textViewAttendance.setText("Total Attendance : " + Long.toString(dataSet.get(listPosition).getAttendance()));
        textViewMarks.setText("Marks Obtained : " + Long.toString(dataSet.get(listPosition).getMarks()));

        holder.itemView.setTag(dataSet.get(listPosition).getRollNo());
    }

    public int getItemCount() {
        return dataSet.size();
    }
}
