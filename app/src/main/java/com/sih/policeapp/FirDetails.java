package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.Utils.SendNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirDetails extends AppCompatActivity implements ExampleDialog.ExampleDialogListner {

    DatabaseReference mRootRef;
    FirebaseUser currentUser;

    TextView name, email, age, gender,phone;

    // access all auto complete text views
    TextView act,state,dist;

    TextView place, subject, details;
    TextView appointmentDate,appointmentTime;

    TextView crimeType,time;
    String error="";
    private TextView accept,reject,viewProfile;

    int flag = 0;
    private ProgressDialog mProgressDialog;

    Button submit;
    String firId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_details);

        firId = getIntent().getStringExtra("fir_id");

        name = findViewById(R.id.complainant_name);
        email = findViewById(R.id.complainant_email);
        age = findViewById(R.id.complainant_age);
        gender = findViewById(R.id.complainant_gender);
        phone = findViewById(R.id.complainant_phone);

        appointmentDate = findViewById(R.id.appointment_date);
        appointmentTime = findViewById(R.id.appointment_time);





        state =findViewById(R.id.occurrence_state);
        dist = findViewById(R.id.occurrence_district);
        place = findViewById(R.id.occurrence_place);

        subject = findViewById(R.id.complain_subject);
        details = findViewById(R.id.complain_details);
        crimeType = findViewById(R.id.crime_type);
        time = findViewById(R.id.time_of_occurrence);

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        viewProfile = findViewById(R.id.view);

        setOnClicks();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        if(firId != null)
        mRootRef.child("FIRs").child(firId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {
                    Map<String, String> map;

                    map = (Map<String, String>) dataSnapshot.getValue();
                    Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                            map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                            map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                    String a,s,d,f,g,h,j;

                    a = "State  : " + fir.getState();
                    s = "City   : " + fir.getDistrict();
                    d = "Address : " + fir.getPlace();

                    state.setText(a);
                    dist.setText(s);
                    place.setText(d);

                    f = "Crime Type : " + fir.getType();
                    g = "Crime     : " + fir.getSubject();
                    h = "Details   : " + fir.getDetails();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());   // 12-03-2019 15:16:17
                    java.util.Date currenTimeZone = new java.util.Date((Long.parseLong(fir.getTs())));
                    String currentDate = DateFormat.getDateInstance().format(new Date());                             //Jun 27, 2017 1:17:32 PM
                    j = sdf.format(currenTimeZone);


                    crimeType.setText(f);
                    subject.setText(g);
                    details.setText(h);
                    time.setText(j);

                    mRootRef.child("Users").child(fir.getComplainantId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                User user = dataSnapshot.getValue(User.class);
                                String a,b,c,d,e,f,g,h;
                                age = findViewById(R.id.complainant_age);
                                gender = findViewById(R.id.complainant_gender);
                                phone = findViewById(R.id.complainant_phone);

                                assert user != null;
                                a = "Name   : " +  user.getName();
                                b = "Email   : " + user.getEmail();
                                c = "Age     : " + user.getAge();
                                d = "Gender : " +user.getGender();
                                e= "Phone   : " + user.getPhone();
                                name.setText(a);
                                email.setText(b);
                                age.setText(c);
                                gender.setText(d);
                                phone.setText(e);


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

    private void setOnClicks() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    new SendNotification("Hello Nishchal!!","Fir Accepted","dbaa7177-7518-4398-83a6-b57ccfc0d299");
                int flag=0;
                if(appointmentDate.getVisibility()==View.GONE || error.equals("yes"))
                {
                    openDialog();
                }else{

                    if(appointmentDate.getVisibility()==View.VISIBLE && error.equals(""))
                    {
                        mRootRef.child("FIRs").child(firId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {

                                    Map<String, String> map;

                                    map = (Map<String, String>) dataSnapshot.getValue();
                                    Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                                            map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                            map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                                    mRootRef.child("Users").child(fir.getComplainantId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                User user = dataSnapshot.getValue(User.class);



                                                assert user != null;
                                                mRootRef.child("FIRs").child(firId).child("correspondent").setValue(user.getName());
                                                mRootRef.child("FIRs").child(firId).child("reportingPlace").setValue(user.getName());
                                                mRootRef.child("FIRs").child(firId).child("reportingDate").setValue(user.getName());
                                                String s = user.getNotificationId();
                                                new SendNotification("Hello " + user.getName() + "!!","Fir Accepted",s);

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
                mRootRef.child("FIRs").child(firId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            Map<String, String> map;

                            map = (Map<String, String>) dataSnapshot.getValue();

                            Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                                    map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                    map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                            mRootRef.child("Users").child(fir.getComplainantId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        User user = dataSnapshot.getValue(User.class);

                                        assert user != null;
                                        String s = user.getNotificationId();
                                        new SendNotification("Sorry for inconvenience " + user.getName() + "!!","Fir Rejected",s);

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
                mRootRef.child("FIRs").child(firId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            Map<String, String> map;

                            map = (Map<String, String>) dataSnapshot.getValue();
                            Fir fir = new Fir(map.get("complainantId"), map.get("state"), map.get("district"), map.get("place"), map.get("type"),
                                    map.get("subject"), map.get("details"), String.valueOf(map.get("timeStamp")), map.get("status"),
                                    map.get("reportingDate"), map.get("reportingPlace"), map.get("correspondent"));

                            Intent intent = new Intent(FirDetails.this,UserProfile.class);
                            intent.putExtra("user_id",fir.getComplainantId());
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
