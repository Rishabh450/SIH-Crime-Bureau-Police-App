package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsHandeling extends AppCompatActivity {

    private static final String TAG = "AppointmentsHandeling";

    RecyclerView recyclerView;
    List<String> appointmentsList;
    
    AppointmentsAdapter appointmentsAdapter;
    String userId;

    String[] category = {"FIRs", "NOC"};

    String txt_category = "FIRs";

    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_handeling);

        userId = getIntent().getStringExtra("user_id");

        recyclerView = findViewById(R.id.appointment_list);

        spinner =findViewById(R.id.spinner);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        appointmentsList = new ArrayList<>();


//        readAppointments(txt_category);
//        appointmentsAdapter = new AppointmentsAdapter(appointmentsList,getApplicationContext(), txt_category);
//
//        recyclerView.setAdapter(appointmentsAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txt_category = category[position];
//                    Log.i(TAG, "onItemSelected: " + txt_category);
                readAppointments(txt_category);

                appointmentsAdapter = new AppointmentsAdapter(appointmentsList, getApplicationContext(), txt_category);

                recyclerView.setAdapter(appointmentsAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, category);
        // setting adapters to spinners
        spinner.setAdapter(spin_adapter);


    }

    private void readAppointments(String category) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(category);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                appointmentsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    appointmentsList.add(snapshot.getKey());

                }

                appointmentsAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
