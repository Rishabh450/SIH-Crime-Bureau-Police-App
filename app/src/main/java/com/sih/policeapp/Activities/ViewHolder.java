package com.sih.policeapp.Activities;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih.policeapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder{
View mview;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
    }
    public void setDetails(Context ctx,String criminal_id,String criminal_name,String profile_pic_url,float rate ){
TextView cid =mview.findViewById(R.id.criminalid);
TextView cname=mview.findViewById(R.id.nameofcriminal);
        RatingBar ratingBar=mview.findViewById(R.id.ratingBar);
ratingBar.setRating(rate);
ratingBar.setEnabled(false);
        ImageView cimg=mview.findViewById(R.id.criminalphoto);
        cid.setText(criminal_id);
        cname.setText(criminal_name);
        Picasso.with(mview.getContext()).load(profile_pic_url).into(cimg);
    }
}

