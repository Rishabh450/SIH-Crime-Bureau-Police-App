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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

        place = findViewById(R.id.occurrence_place);

        state =findViewById(R.id.occurrence_state);
        dist = findViewById(R.id.occurrence_district);

        subject = findViewById(R.id.complain_subject);
        details = findViewById(R.id.complain_details);
        crimeType = findViewById(R.id.crime_type);
        time = findViewById(R.id.time_of_occurrence);



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
//                    mRootRef.child("Users").child(fi)


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
