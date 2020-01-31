package com.sih.policeapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CriminalProfileAdapter extends RecyclerView.Adapter<CriminalProfileAdapter.ViewHolder> {

    private ArrayList<String> crimeList;
    private Context ctx;
    private String currCriminal;

    public CriminalProfileAdapter(ArrayList<String> crimeList, Context ctx, String currCriminal) {
        this.crimeList = crimeList;
        this.ctx = ctx;
        this.currCriminal = currCriminal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==2){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.criminals_profile_top_view, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_crime_layout, parent, false);
        }

        return new CriminalProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Button addNewCrime;
        addNewCrime =   holder.mView.findViewById(R.id.add_crime);
        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        addNewCrime.setVisibility(View.GONE);

        if(position==crimeList.size())
        {

          addNewCrime.setVisibility(View.VISIBLE);
          addNewCrime.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(ctx,AddSingleCrime.class);
                  intent.putExtra("curr_criminal",currCriminal);
                  ctx.startActivity(intent);
              }
          });
        }
        if(position==0)
        {
            final CircleImageView profile_pic;
            final TextView displayName,BodyMark,DOB,AddressOfCriminal,Rating_of_criminal;
            profile_pic = holder.mView.findViewById(R.id.criminal_profile_pic);
            displayName = holder.mView.findViewById(R.id.criminal_name);
            BodyMark = holder.mView.findViewById(R.id.body_mark);
            DOB = holder.mView.findViewById(R.id.DOB);
            AddressOfCriminal = holder.mView.findViewById(R.id.address_of_criminal);
            Rating_of_criminal = holder.mView.findViewById(R.id.criminals_rating);


            mRootRef.child("criminal_ref").child(currCriminal).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Criminals currCriminal = dataSnapshot.getValue(Criminals.class);

                        Picasso.with(ctx)
                                .load(currCriminal.getProfile_pic_url())
                                .placeholder(R.drawable.avtar)
                                .into(profile_pic);
                        String a = "Name            :" + currCriminal.getCriminal_name();
                        String b = "Body Mark    :" + currCriminal.getCriminal_BodyMark();
                        String c = "DOB               :" + currCriminal.getCriminals_DOB();
                        String d =  currCriminal.getCriminal_address();
                        String e = "Name          :" + currCriminal.getCriminal_name();
                        String f = "Rating of Criminal  :" + currCriminal.getCriminal_rating();

                        String rat = currCriminal.getCriminal_rating();
                        if(rat.equals("1")||rat.equals("2")) ;
                        else if(rat.equals("3")) Rating_of_criminal.setTextColor(Color.parseColor("#6c6c6c"));  //darkGrey
                        else if(rat.equals("5")) Rating_of_criminal.setTextColor(Color.parseColor("#E01010"));  //Red
                        else if(rat.equals("4")) Rating_of_criminal.setTextColor(Color.parseColor("#F57C00"));  //Orange

                        displayName.setText(a);
                        BodyMark.setText(b);
                        DOB.setText(c);
                        AddressOfCriminal.setText(d);
                        Rating_of_criminal.setText(f);



                    } else {
                        Toast.makeText(ctx, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                        ((Activity)ctx).finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            final TextView crimeType,crime,state,district,DOC,addressOfCrime,crimeRating,crimeStatus ;
            crimeType = holder.mView.findViewById(R.id.main_crime_type);
            crime = holder.mView.findViewById(R.id.crime_type);
            state = holder.mView.findViewById(R.id.state);
            district = holder.mView.findViewById(R.id.district);
            DOC = holder.mView.findViewById(R.id.DOC);
            addressOfCrime = holder.mView.findViewById(R.id.crime_full_adress);
            crimeRating = holder.mView.findViewById(R.id.rating_of_crime);
            crimeStatus = holder.mView.findViewById(R.id.case_status);

            mRootRef.child("crime_ref").child(crimeList.get(position-1)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Crime currCrime = dataSnapshot.getValue(Crime.class);
                        assert currCrime != null;
                        String a = "Crime Type    :  " + currCrime.getMain_crime_type();
                        String b = "Crime :  " + currCrime.getCrime_type();
                        String c = "State :  " + currCrime.getState_of_crime();
                        String d = "District :  " + currCrime.getDistrict_of_crime();
                        String e = "Date Of Crime :  " + currCrime.getDate_of_crime();
                        String f = currCrime.getAddress_of_crime();
                        String g = "Rating of Crime :  " + currCrime.getRating_of_crime();
                        String h = "Crime Status :  " + currCrime.getCase_status();
                       crimeType.setText(a);
                        crime.setText(b);
                        state.setText(c);
                        district.setText(d);
                        DOC.setText(e);
                        addressOfCrime.setText(f);
                        crimeRating.setText(g);
                        crimeStatus.setText(h);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return crimeList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 2;
        }
        else{
            return 3;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}
