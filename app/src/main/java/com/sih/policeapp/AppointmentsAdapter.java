package com.sih.policeapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private static final String TAG = "AppointmentsAdapter";

    List<String> mAppointments;
    Context mContext;
    String string, category;
    User user = null;

    DatabaseReference databaseReference;

    public AppointmentsAdapter(List<String> mAppointments, Context mContext, String category) {
        this.mAppointments = mAppointments;
        this.mContext = mContext;
        this.category = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.single_appointment_view, parent, false);

        return new AppointmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final String string = mAppointments.get(position);


        databaseReference = FirebaseDatabase.getInstance().getReference(category).child(string);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(category.equals("FIRs") && dataSnapshot.exists()){

//                    Toast.makeText(mContext, "Here", Toast.LENGTH_SHORT).show();

                    Map<String, String> map;

//                    Log.i(TAG, "onDataChange: "+string);

                    map = (Map<String, String>) dataSnapshot.getValue();
//
                    final Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                            map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                            map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));
//
////                    Log.i(TAG, "onDataChange: "+ fir.getTs());
//                    fir = dataSnapshot.getValue(Fir.class);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(fir.getComplainantId());

                    Log.i(TAG, "onDataChange: "+fir.getComplainantId());

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            user = dataSnapshot.getValue(User.class);
                            holder.FIRorNoc.setText("FIR");
                            holder.name.setText(user.getName());
                            holder.crimeType.setText(fir.getType());

                            holder.crime.setText(fir.getDetails());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }else if(category.equals("NOC") && dataSnapshot.exists()){


                    Map<String, String> map;

                    map = (Map<String, String>) dataSnapshot.getValue();

                    if(dataSnapshot.getChildrenCount() == 18){

                        final Noc noc = new Noc(map.get("surname"), map.get("name"), map.get("presentAddress"), map.get("homeAddress"),
                                map.get("dateOfBirth"), map.get("placeOfBirth"), map.get("nocType"), map.get("charges"),
                                map.get("identificationMark"), map.get("fatherName"), map.get("motherName"), map.get("spouseName"),
                                map.get("userId"), String.valueOf(map.get("timeStamp")), map.get("status"), map.get("reportingDate"),
                                map.get("reportingPlace"), map.get("correspondent"));

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(noc.getUserId());

                        Log.i(TAG, "onDataChange: "+ noc.getUserId());

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                user = dataSnapshot.getValue(User.class);
                                holder.FIRorNoc.setText("NOC");
                                holder.name.setText(user.getName());
                                holder.crimeType.setText(noc.getNocType());
                                holder.crime.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


//                        holder.nocStatus.setText(noc.getStatus());


                    }
                    else{

                        final Noc noc = new Noc(map.get("surname"), map.get("name"), map.get("presentAddress"), map.get("homeAddress"),
                                map.get("dateOfBirth"), map.get("placeOfBirth"), map.get("nocType"), map.get("rcNumber"),
                                map.get("icNumber"), map.get("etNumber"), map.get("userId"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(noc.getUserId());

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()) {
                                    user = dataSnapshot.getValue(User.class);
                                    holder.FIRorNoc.setText("NOC");
                                    holder.name.setText(user.getName());
                                    holder.crimeType.setText(noc.getNocType());
                                    holder.crime.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(category.equals("FIRs")) {

                    Intent intent = new Intent(mContext, FirDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("fir_id", string);
                    mContext.startActivity(intent);

                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView FIRorNoc, name, crimeType, crime, accept, reject, view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            FIRorNoc = itemView.findViewById(R.id.FIRorNoc);
            name = itemView.findViewById(R.id.name);
            crimeType = itemView.findViewById(R.id.crime_type);
            crime = itemView.findViewById(R.id.crime);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
            view = itemView.findViewById(R.id.view);

        }

    }

}
