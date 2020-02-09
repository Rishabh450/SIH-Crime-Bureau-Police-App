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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.Utils.SendNotification;

import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private static final String TAG = "AppointmentsAdapter";

    List<String> mAppointments;
    Context mContext;
    String string, category;
    User user = null;

    DatabaseReference databaseReference, mRootRef;

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

        mRootRef = FirebaseDatabase.getInstance().getReference();


        databaseReference = FirebaseDatabase.getInstance().getReference(category).child(string);

        if (category.equals("FIRs")) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {


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

                        Log.i(TAG, "onDataChange: " + fir.getComplainantId());

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


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mRootRef.child("FIRs").child(string).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                Map<String, String> map;

                                map = (Map<String, String>) dataSnapshot.getValue();
                                final Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                                        map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                        map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                                mRootRef.child("Users").child(fir.getComplainantId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            final User user = dataSnapshot.getValue(User.class);

                                            assert user != null;
                                            final String s = user.getNotificationId();

                                            new SendNotification("Hello " + user.getName() + "!!", "Fir Accepted", s);

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


                }
            });


            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mRootRef.child("FIRs").child(string).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                Map<String, String> map;

                                map = (Map<String, String>) dataSnapshot.getValue();

                                Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                                        map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                        map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                                mRootRef.child("Users").child(fir.getComplainantId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            final User user = dataSnapshot.getValue(User.class);

                                            assert user != null;
                                            final String s = user.getNotificationId();


                                            mRootRef.child("FIRs").child(string).child("status").setValue("Rejected")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Log.i(TAG, "onComplete: here");

                                                            new SendNotification("Sorry for inconvenience " + user.getName() + "!!", "Fir Rejected", s);


                                                        }
                                                    });


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

                }
            });


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, FirDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("fir_id", string);
                    mContext.startActivity(intent);

                }
            });


        } else {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {


                        Map<String, String> map;

                        map = (Map<String, String>) dataSnapshot.getValue();

                        final Noc noc = dataSnapshot.getValue(Noc.class);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(noc.getUserId());

                        Log.i(TAG, "onDataChange: " + noc.getUserId());

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


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mRootRef.child("NOC").child(string).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                Noc noc = dataSnapshot.getValue(Noc.class);

                                assert noc != null;
                                mRootRef.child("Users").child(noc.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            User user = dataSnapshot.getValue(User.class);

                                            assert user != null;
                                            String s = user.getNotificationId();
                                            new SendNotification("Hello " + user.getName() + "!!", "NOC Accepted", s);

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


                }
            });



            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mRootRef.child("NOC").child(string).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                Noc noc = dataSnapshot.getValue(Noc.class);

                                assert noc != null;
                                mRootRef.child("Users").child(noc.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            User user = dataSnapshot.getValue(User.class);

                                            assert user != null;
                                            String s = user.getNotificationId();
                                            new SendNotification("Sorry for inconvenience " + user.getName() + "!!", "NOC Rejected", s);

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


                }
            });



            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    Intent intent = new Intent(mContext, NOCdetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("noc_id", string);
                    mContext.startActivity(intent);


                }
            });



        }

    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
