package com.sih.policeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CriminalListAdapter extends RecyclerView.Adapter<CriminalListAdapter.ViewHolder> {

    private Context ctx;
    private List<Criminals> shortlistedCriminals;


    public CriminalListAdapter(Context ctx, List<Criminals> shortlistedCriminals) {
        this.ctx = ctx;
        this.shortlistedCriminals = shortlistedCriminals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_criminal_list_view, parent, false);
        return new CriminalListAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CircleImageView circleImageView;
        final TextView name,rating,lastCrime,viewProfile;

        circleImageView = holder.mView.findViewById(R.id.criminal_profile_pic);
        name = holder.mView.findViewById(R.id.criminal_name);
        rating = holder.mView.findViewById(R.id.criminals_rating);
        lastCrime = holder.mView.findViewById(R.id.last_crime);
        viewProfile = holder.mView.findViewById(R.id.view_profile);
        final DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        final Criminals currCriminal = shortlistedCriminals.get(position);

        Picasso.with(ctx)
                .load(currCriminal.getProfile_pic_url())
                .placeholder(R.drawable.avtar)
                .into(circleImageView);

        name.setText(currCriminal.getCriminal_name());
        String rat = currCriminal.getCriminal_rating();
        if(rat.equals("1")||rat.equals("2")) ;
        else if(rat.equals("3")) rating.setTextColor(Color.parseColor("#6c6c6c"));  //darkGrey
        else if(rat.equals("5")) rating.setTextColor(Color.parseColor("#E01010"));  //Red
        else if(rat.equals("4")) rating.setTextColor(Color.parseColor("#F57C00"));  //Orange
        String ans = "Criminal Rating: " + currCriminal.getCriminal_rating();
        rating.setText(ans);
       // lastCrime

        mRootRef.child("criminal_ref").child(currCriminal.getCriminal_id()).child("last_crime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String req = dataSnapshot.getValue(String.class);
                    mRootRef.child("crime_ref").child(req).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Crime currCrime = dataSnapshot.getValue(Crime.class);
                                String res = currCrime.getMain_crime_type() + "( " + currCrime.getCrime_type() + "," + " " + "in " + currCrime.getDistrict_of_crime() + "," +currCrime.getState_of_crime() +" )";
                                lastCrime.setText(res);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currCriminalID;
                Intent intent = new Intent(ctx,CriminalProfile.class);
                intent.putExtra("curr_criminal",currCriminal.getCriminal_id());
                ctx.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return shortlistedCriminals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}
