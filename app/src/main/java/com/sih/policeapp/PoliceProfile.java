package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PoliceProfile extends AppCompatActivity {

    private static final String TAG = "PoliceProfile";

    private CircleImageView circleImageView;
    private TextView name,EmailID,policeName;
    private TextView designation,policeStationID;
    private TextView policeID;
    private TextView act;
    private ArrayList<String> listCity=new ArrayList<String>();
    private ArrayAdapter<CharSequence> adapter;
    private RadioGroup radioGroup;
    private RadioButton radioButton,radioButton2;
    private Button edit;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mRef;
    private RatingBar ratingBar;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_profile);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        circleImageView  = findViewById(R.id.police_profile_pic);
        name = findViewById(R.id.police_name);
        EmailID = findViewById(R.id.email_ID);
        act = findViewById(R.id.posted_city);
        radioGroup = findViewById(R.id.radio_button_group);
        designation = findViewById(R.id.designation);
        policeStationID = findViewById(R.id.policeStation);
        ratingBar = findViewById(R.id.rating_of_police);
        edit = findViewById(R.id.edit);

        userID = getIntent().getStringExtra("user_id");

        if(!userID.equals(FirebaseAuth.getInstance().getUid()))
        {
           edit.setVisibility(View.GONE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PoliceProfile.this, RegisterActivity.class));
                finish();
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        mRef = FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(userID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    PoliceClass police = dataSnapshot.getValue(PoliceClass.class);
                    assert police != null;
                    Picasso.with(getApplicationContext())
                            .load(police.getProfile_pic())
                            .placeholder(R.drawable.avtar)
                            .into(circleImageView);
                    String nam = "Name : " + police.getPolice_name();
                    String deg = police.getDesignation();
                    String ps = police.getPoliceStationId();
                    String email = "Email: " +police.getEmail_id();
                    String city = "Posting: " + police.getPosted_city();
                    String radio;

                    radio = police.getPsHead();
                    radioButton = findViewById(R.id.YES);
                    radioButton2 = findViewById(R.id.NO);
                    if(radio.equals("YES"))
                    {

                        radioButton.setChecked(true);
                        radioButton2.setEnabled(false);

                    }else{

                        radioButton2.setChecked(true);
                        radioButton.setEnabled(false);
                    }


                    name.setText(nam);
                    designation.setText(deg);
                    policeStationID.setText(ps);
                    EmailID.setText(email);
                    act.setText(city);
                    ratingBar.setRating(Float.parseFloat(police.getRating()));
                    ratingBar.setEnabled(false);
                     mProgressDialog.hide();
//                    name.setText();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
