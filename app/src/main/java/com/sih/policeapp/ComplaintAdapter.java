package com.sih.policeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    Context mContext;
    List<Complaint> mComplaint;

    DatabaseReference databaseReference;

    public ComplaintAdapter(Context mContext, List<Complaint> mComplaint) {
        this.mContext = mContext;
        this.mComplaint = mComplaint;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.complaint_layout, parent, false);

        return new ComplaintAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Complaint complaint = mComplaint.get(position);

        holder.post.setText(complaint.getLevel());
        holder.details.setText(complaint.getDetails());


        long milliseconds = complaint.getTimeStamp();
        String simpleDateFormat = DateFormat.getDateTimeInstance().format(milliseconds);
        holder.date.setText(simpleDateFormat);


    }

    @Override
    public int getItemCount() {
        return mComplaint.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date, post, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.complaintDate);
            post = itemView.findViewById(R.id.complaintPost);
            details = itemView.findViewById(R.id.complaintDetails);

        }
    }

}
