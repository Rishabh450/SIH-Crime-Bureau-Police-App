package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CriminalProfile extends AppCompatActivity {
    private static final String TAG = "CriminalProfile";

    private RecyclerView recyclerView ;
    private ArrayList<String> crimeList;
    RecyclerView.Adapter mAdapter;
    DatabaseReference mRootRef;
    String currCriminalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal_profile);
        recyclerView = findViewById(R.id.criminal_profile_rv);
        crimeList = new ArrayList<>();
        currCriminalID = getIntent().getStringExtra("curr_criminal");
//        crimeList.add("asdfg");
//        crimeList.add("shj");

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("criminal_crimes_relation_ref").child(currCriminalID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    crimeList.add(dataSnapshot1.getKey());
                }
              //  Log.i(TAG, "onDataChange: " + crimeList.toString());

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new CriminalProfileAdapter(crimeList,CriminalProfile.this,currCriminalID);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
