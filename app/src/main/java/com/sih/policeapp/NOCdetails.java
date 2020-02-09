package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NOCdetails extends AppCompatActivity {
    private static final String TAG = "NOCdetails";

    String nocID;
    TextView surname, name, presentAddress, homeAddress, dateOfBirth, placeOfBirth, charges, identificationMark, fatherName, motherName
            ,spouseName, rcNumber, icNumber, etNumber;
    TextView spinner;
    Button submit;
    LinearLayout passport, vehicle;
    String nocType;

    String[] type = {"Passport", "Re-registering Vehicle"};

     private  DatabaseReference mRootRef;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nocdetails);
        nocID = getIntent().getStringExtra("noc_id");

//        surname = findViewById(R.id.nocSurname);
//        name = findViewById(R.id.nocName);
//        presentAddress = findViewById(R.id.nocPresentAddress);
//        homeAddress = findViewById(R.id.nocHomeAddress);
//        dateOfBirth = findViewById(R.id.nocDateOfBirth);
//        placeOfBirth = findViewById(R.id.nocPlaceOfBirth);
//        charges = findViewById(R.id.nocCharges);
//        fatherName =findViewById(R.id.nocFatherName);
//        motherName = findViewById(R.id.nocIdentificationMark);
//        identificationMark = findViewById(R.id.nocMotherName);
//        spouseName = findViewById(R.id.nocSpouseName);
//        rcNumber = findViewById(R.id.nocRC);
//        icNumber = findViewById(R.id.nocInsuranceCertificate);
//        etNumber = findViewById(R.id.nocEmissionTest);
//        spinner = findViewById(R.id.nocSpinner);
//        passport = findViewById(R.id.nocPassport);
//        vehicle = findViewById(R.id.nocVehicle);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("NOC").child(nocID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Noc noc = dataSnapshot.getValue(Noc.class);

                    assert noc != null;
                    Log.i(TAG, "onDataChange547: "+noc.getTimeStamp().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
