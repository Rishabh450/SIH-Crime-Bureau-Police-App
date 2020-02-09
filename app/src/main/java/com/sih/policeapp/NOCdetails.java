package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.sih.Utils.SendNotification;

import java.util.HashMap;
import java.util.Map;

public class NOCdetails extends AppCompatActivity {
    private static final String TAG = "NOCdetails";

    String nocID;
    TextView surname, name, presentAddress, homeAddress, dateOfBirth, placeOfBirth, charges, identificationMark, fatherName, motherName
            ,spouseName, rcNumber, icNumber, etNumber;
    TextView spinner;
    Button submit;
    LinearLayout passport, vehicle;
    String nocType;
    private TextView accept,reject,viewProfile;

    String[] type = {"Passport", "Re-registering Vehicle"};
    private ProgressDialog mProgressDialog;

     private  DatabaseReference mRootRef;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nocdetails);
        nocID = getIntent().getStringExtra("noc_id");

        name = findViewById(R.id.nocName);
        presentAddress = findViewById(R.id.nocPresentAddress);
        homeAddress = findViewById(R.id.nocHomeAddress);
        dateOfBirth = findViewById(R.id.nocDateOfBirth);
        placeOfBirth = findViewById(R.id.nocPlaceOfBirth);
        charges = findViewById(R.id.nocCharges);
        fatherName =findViewById(R.id.nocFatherName);
        motherName = findViewById(R.id.nocMotherName);
        identificationMark = findViewById(R.id.nocIdentificationMark);
        spouseName = findViewById(R.id.nocSpouseName);
        rcNumber = findViewById(R.id.nocRC);
        icNumber = findViewById(R.id.nocInsuranceCertificate);
        etNumber = findViewById(R.id.nocEmissionTest);
        spinner = findViewById(R.id.nocSpinner);
        passport = findViewById(R.id.nocPassport);
        vehicle = findViewById(R.id.nocVehicle);

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        viewProfile = findViewById(R.id.view);



        mRootRef = FirebaseDatabase.getInstance().getReference();

        setOnClicks();

        mRootRef.child("NOC").child(nocID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Noc noc = dataSnapshot.getValue(Noc.class);

                    assert noc != null;
                    String surnam, nam, presentAddres, homeAddres, dateOfBirt, placeOfBirt, nocTyp, charge, identificationMak, fatheName, motheName
                            ,sposeName, rNumber, icNuber, etNmber, usrId, ts;
                    String stats, reportngDate, reporingPlace, corresondent;
                    Long timeStamp;

                    nam           ="Name  : " + noc.getName() + " " + noc.getSurname();
                    presentAddres ="Present Address :" + noc.getPresentAddress();
                    homeAddres    ="Home Address    :" + noc.getHomeAddress();
                    dateOfBirt    ="DOB   : " + noc.getDateOfBirth();
                    placeOfBirt   ="Place of Birth :" + noc.getPlaceOfBirth();

                    name.setText(nam);
                    presentAddress.setText(presentAddres);
                    homeAddress.setText(homeAddres);
                    dateOfBirth.setText(dateOfBirt);
                    placeOfBirth.setText(placeOfBirt);

                    if(noc.getNocType().equals("Passport"))
                    {
                        passport.setVisibility(View.VISIBLE);
                        vehicle.setVisibility(View.GONE);

                        spinner.setText("Passport");

                        charge = noc.getCharges();
                        identificationMak = noc.getIdentificationMark();
                        fatheName = noc.getFatherName();
                        motheName = noc.getMotherName();

                        charges.setText(charge);
                        identificationMark.setText(identificationMak);
                        fatherName.setText(fatheName);
                        motherName.setText(motheName);

                        mProgressDialog.hide();

                    }else{
                        spinner.setText("Re-registering Vehicle");
                        passport.setVisibility(View.GONE);
                        vehicle.setVisibility(View.VISIBLE);

                        rNumber = "RC Number : " + noc.getRcNumber();
                        icNuber = "IC Number : " + noc.getIcNumber();
                        etNmber = "ET Number : " + noc.getEtNumber();

                        rcNumber.setText(rNumber);
                        icNumber.setText(icNuber);
                        etNumber.setText(etNmber);


                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setOnClicks() {

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    new SendNotification("Hello Nishchal!!","Fir Accepted","dbaa7177-7518-4398-83a6-b57ccfc0d299");

                mRootRef.child("NOC").child(nocID).addValueEventListener(new ValueEventListener() {
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
                                        Toast.makeText(NOCdetails.this, "NOC Accepted!!..", Toast.LENGTH_SHORT).show();
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
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootRef.child("NOC").child(nocID).addValueEventListener(new ValueEventListener() {
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
                                        Toast.makeText(NOCdetails.this, "NOC Rejected!!", Toast.LENGTH_SHORT).show();
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
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootRef.child("NOC").child(nocID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Noc noc = dataSnapshot.getValue(Noc.class);

                            Intent intent = new Intent(NOCdetails.this,UserProfile.class);
                            assert noc != null;
                            intent.putExtra("user_id",noc.getUserId());
                            startActivity(intent);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}

