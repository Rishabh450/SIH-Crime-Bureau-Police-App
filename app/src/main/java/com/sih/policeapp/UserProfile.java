package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private String userId;
    private CircleImageView profilePicture;
    private TextView name, email;
    private TextView age, fathername, address, pin, phone, fax, aadhaar;
    private Button submit;
    FirebaseUser currentUser;
    DatabaseReference mRootRef;
    RadioGroup radioGroup;
    User user;
    private static final String TAG = "AccountFragment";
    String gender = "";
    int flag = 0;
    View view;
    RadioButton lastButton;
    RadioButton radioButton,radioButton2,radioButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userId = getIntent().getStringExtra("user_id");

        profilePicture = findViewById(R.id.accountProfilePicture);
        name = findViewById(R.id.label_displayName);
        email = findViewById(R.id.label_displayEmail);
        age = findViewById(R.id.edit_age);
        fathername = findViewById(R.id.edit_fathersname);
        address = findViewById(R.id.edit_address);
        pin = findViewById(R.id.edit_pin);
        phone = findViewById(R.id.edit_phone);
        fax = findViewById(R.id.edit_fax);
        aadhaar = findViewById(R.id.edit_aadhaar);
        radioGroup = findViewById(R.id.radioGroup);
        lastButton = findViewById(R.id.radio_others);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    User user = dataSnapshot.getValue(User.class);

                    String names, emails, imageURL, gender, fatherName, addresss, ages, pincodes, phones, faxs , aadhaars;

                    assert user != null;
                    names      ="Name    :" +user.getName().trim();
                    emails     ="EmailID :" +user.getEmail();
                    imageURL   = user.getImageURL();
                    fatherName ="Father   :" +user.getFathername();
                    addresss   ="Address :" +user.getAddress();
                    ages       ="Age      :" +user.getAge();
                    pincodes   ="Pincode :"+ user.getPincode();
                    phones     ="Phone   :"+ user.getPhone();
                    faxs       ="Fax     :" + user.getFax();
                    aadhaars   ="Aadhaar :" + user.getAadhaar();
                    gender     =user.getGender().trim();

                    radioButton = findViewById(R.id.radio_male);
                    radioButton2 = findViewById(R.id.radio_female);
                    radioButton3 = findViewById(R.id.radio_others);
                    Log.i(TAG, "onDataChange: ");
                    if(gender.equals("Male"))
                    {
                        radioButton.setChecked(true);
                        radioButton2.setEnabled(false);
                        radioButton3.setEnabled(false);

                    }else if(gender.equals("Female")){
                        radioButton.setEnabled(false);
                        radioButton2.setChecked(true);
                        radioButton3.setEnabled(false);
                    }else{
                        radioButton.setEnabled(false);
                        radioButton2.setEnabled(false);
                        radioButton3.setChecked(true);
                    }


                    Picasso.with(getApplicationContext())
                            .load(imageURL)
                            .placeholder(R.drawable.avtar)
                            .into(profilePicture);
                    name.setText(names);
                    email.setText(emails);
                    fathername.setText(fatherName);
                    address.setText(addresss);
                    age.setText(ages);
                    pin.setText(pincodes);
                    phone.setText(phones);
                    fax.setText(faxs);
                    aadhaar.setText(aadhaars);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
