package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class FirDetails extends AppCompatActivity {

    DatabaseReference mRootRef;
    FirebaseUser currentUser;

    TextView name, email, age, gender,phone;

    // access all auto complete text views
    TextView act,state,dist;

    TextView place, subject, details;

    // array lists
    // for the spinner in the format : City_no : City , State. Eg : 144 : New Delhi , India
    ArrayList<String> listSpinner=new ArrayList<String>();
    // to store the city and state in the format : City , State. Eg: New Delhi , India
    ArrayList<String> listAll=new ArrayList<String>();
    // for listing all states
    ArrayList<String> listState=new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity=new ArrayList<String>();
    View view;


    Map<String , ArrayList<String>> stateDistrict;
    Map<String , ArrayList<String>> districtLocality = new HashMap<>();


    TextView crimeType,time;
    private TextView accept,reject,viewProfile;


    int flag = 0;


    Button submit;
    String firId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_details);

        obj_list();


        firId = getIntent().getStringExtra("fir_id");

        name = findViewById(R.id.complainant_name);
        email = findViewById(R.id.complainant_email);
        age = findViewById(R.id.complainant_age);
        gender = findViewById(R.id.complainant_gender);
        phone = findViewById(R.id.complainant_phone);


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
                new SendNotification("Message 1","heading 1","dbaa7177-7518-4398-83a6-b57ccfc0d299");
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // Get the content of cities.json from assets directory and store it as string
    public String getJson()
    {
        String json=null;
        try
        {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list()
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                listCity.add(city);
                listState.add(state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
