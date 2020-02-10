package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComplaintActivity extends AppCompatActivity {

    String myLevel;

    String[] level = {"Select Your Designation", "Director General of Police (DGP)", "Special Director General of Police (SDG)",
            "Additional Director General of Police (ADG)", "Inspector General of Police (IG)", "Deputy Inspector General of Police (DIG)",
            "Senior Superintendent of Police (SSP)", "Superintendent of Police (SP)", "Deputy Superintendent of Police (Dy.SP)",
            "Assistant Superintendent of Police (ASP)", "Inspector", "Constable"};

    RecyclerView recyclerView;
    ComplaintAdapter complaintAdapter;
    List<Complaint> mComplaint;

    DatabaseReference databaseReference;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        recyclerView = findViewById(R.id.complaintRecyclerView);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PoliceUser").child(currentUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    PoliceClass policeClass = dataSnapshot.getValue(PoliceClass.class);

                    myLevel = policeClass.getDesignation();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);


        if (currentUser != null) {

            mComplaint = new ArrayList<>();

            readComplaint();
            complaintAdapter = new ComplaintAdapter(getApplicationContext(), mComplaint);
            recyclerView.setAdapter(complaintAdapter);


        }


    }

    private void readComplaint() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Complainbox");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Complaint complaint = snapshot.getValue(Complaint.class);

                        if (getId(complaint.getLevel()) < getId(myLevel)) {

                            mComplaint.add(complaint);

                        }

                    }

                }

                complaintAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public int getId(String designation) {

        if (designation.equals("Director General of Police (DGP)")) return 10;

        if (designation.equals("Special Director General of Police (SDG)")) return 9;

        if (designation.equals("Additional Director General of Police (ADG)")) return 8;

        if (designation.equals("Inspector General of Police (IG)")) return 7;

        if (designation.equals("Deputy Inspector General of Police (DIG)")) return 6;

        if (designation.equals("Senior Superintendent of Police (SSP)")) return 5;

        if (designation.equals("Superintendent of Police (SP)")) return 4;

        if (designation.equals("Deputy Superintendent of Police (Dy.SP)")) return 3;

        if (designation.equals("Assistant Superintendent of Police (ASP)")) return 2;

        if (designation.equals("Inspector")) return 1;

        else return 0;

    }

}
