package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Objects;

public class NOCdetails extends AppCompatActivity implements ExampleDialog.ExampleDialogListner {
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
    TextView appointmentDate,appointmentTime;
    String error = "";


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



        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);



        mRootRef = FirebaseDatabase.getInstance().getReference();

        setOnClicks();

        mRootRef.child("NOC").child(nocID).addListenerForSingleValueEvent(new ValueEventListener() {
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

                final int[] notif = {1};

                int flag=0;
                if(appointmentDate.getVisibility()==View.GONE || error.equals("yes"))
                {
                    openDialog();
                }else{

                    if(appointmentDate.getVisibility()==View.VISIBLE && error.equals(""))
                    {
                        mRootRef.child("NOC").child(nocID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {

                                    Noc noc = dataSnapshot.getValue(Noc.class);

                                    assert noc != null;
                                    mRootRef.child("Users").child(noc.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                final User user = dataSnapshot.getValue(User.class);

                                                assert user != null;
                                                final String s = user.getNotificationId();


                                                mRootRef.child("NOC").child(nocID).child("status").setValue("Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if(notif[0] ==1)
                                                        {
                                                            new SendNotification("Hello " + user.getName() + "!!","NOC Accepted..",s);
                                                            notif[0] =0;
                                                        }

                                                        finish();
                                                    }
                                                });



                                                mRootRef.child("PoliceUser").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.exists())
                                                                {
                                                                    PoliceClass police = dataSnapshot.getValue(PoliceClass.class);
                                                                    mRootRef.child("NOC").child(nocID).child("reportingDate").setValue(appointmentDate.getText().toString() + " " + appointmentTime.getText().toString());
                                                                    assert police != null;
                                                                    mRootRef.child("NOC").child(nocID).child("reportingPlace").setValue(police.getPosted_city());
                                                                    mRootRef.child("NOC").child(nocID).child("correspondent").setValue(police.getPolice_name());


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

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] notif = {1};

                int flag=0;


                        mRootRef.child("NOC").child(nocID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {

                                    Noc noc = dataSnapshot.getValue(Noc.class);

                                    assert noc != null;
                                    mRootRef.child("Users").child(noc.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                final User user = dataSnapshot.getValue(User.class);

                                                assert user != null;
                                                final String s = user.getNotificationId();


                                                mRootRef.child("NOC").child(nocID).child("status").setValue("Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if(notif[0] ==1)
                                                        {
                                                            new SendNotification("Sorry for inconvenience " + user.getName() + "!!","NOC Rejected..",s);
                                                            notif[0] =0;
                                                        }

                                                        finish();
                                                    }
                                                });



                                                mRootRef.child("PoliceUser").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.exists())
                                                                {
                                                                    PoliceClass police = dataSnapshot.getValue(PoliceClass.class);
                                                                    mRootRef.child("NOC").child(nocID).child("reportingDate").setValue(appointmentDate.getText().toString() + " " + appointmentTime.getText().toString());
                                                                    assert police != null;
                                                                    mRootRef.child("NOC").child(nocID).child("reportingPlace").setValue(police.getPosted_city());
                                                                    mRootRef.child("NOC").child(nocID).child("correspondent").setValue(police.getPolice_name());


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
                mRootRef.child("NOC").child(nocID).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void openDialog() {
        ExampleDialog exampleDialod = new ExampleDialog();
        exampleDialod.show(getSupportFragmentManager(),"example dialog");
    }

    @Override
    public void applyText(String a, String b) {
        appointmentDate.setVisibility(View.VISIBLE);
        appointmentTime.setVisibility(View.VISIBLE);
        appointmentTime.setText(b); if(!b.equals("")) appointmentTime.setError(null); else appointmentTime.setError("Enter again");
        appointmentDate.setText(a); if(!a.equals("")) appointmentDate.setError(null); else appointmentDate.setError("Enter again");
        if(!a.equals("") && !b.equals(""))
        {
            error = "";
        }else{
            error="yes";
        }
    }
}

