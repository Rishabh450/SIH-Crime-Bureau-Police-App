package com.sih.policeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.policeapp.Activities.Login;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private CircleImageView circleImageView;
    private EditText name,EmailID,policeName;
    private Spinner designation,policeStationID;
    private String policeID;
    private AutoCompleteTextView act;
    private ArrayList<String> listCity=new ArrayList<String>();
    private ArrayAdapter<CharSequence> adapter;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button proceed;
    private ProgressDialog mProgressDialog;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressDialog = new ProgressDialog(this);
        circleImageView  = findViewById(R.id.police_profile_pic);
        name = findViewById(R.id.police_name);
        EmailID = findViewById(R.id.email_ID);
        act = findViewById(R.id.posted_city);
        radioGroup = findViewById(R.id.radio_button_group);
        proceed = findViewById(R.id.proceed);







        obj_list();

        designation = findViewById(R.id.designation);
        adapter = ArrayAdapter.createFromResource(this,R.array.PoliceDesignation,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designation.setAdapter(adapter);

        policeStationID = findViewById(R.id.policeStation);
        adapter = ArrayAdapter.createFromResource(this,R.array.PoliceStations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        policeStationID.setAdapter(adapter);

        Set<String> set = new HashSet<String>(listCity);
        ArrayList arrayList = new ArrayList(listCity);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,arrayList);
        act.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            policeID = FirebaseAuth.getInstance().getUid();
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if(acct != null)
            {
                name.setText(acct.getDisplayName());
                Picasso.with(this)
                        .load(acct.getPhotoUrl())
                        .into(circleImageView);
                EmailID.setText(acct.getEmail());
                EmailID.setFocusable(false);
            }
            setOnclick();

        }
        else{
            Intent intent = new Intent(RegisterActivity.this, Login.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setOnclick() {

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if(acct != null)
                {
                   DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("PoliceUser");

                    Picasso.with(getApplicationContext())
                            .load(acct.getPhotoUrl())
                            .into(circleImageView);
                  //  Log.i(TAG, "setOnclick: " + acct.getPhotoUrl());
                    EmailID.setFocusable(false);
                    String policeName,profile_pic,email_id,rating, designation1,crimeSolved,policeStationId,posted_city,Notification,police_name,psHead;
                    int flag=0;
                    policeName = name.getText().toString().trim();
                    email_id = acct.getEmail();

                    Map<String,String> mp = new HashMap<>();
                    mp.put("profile_pic", Objects.requireNonNull(acct.getPhotoUrl()).toString());
                    assert email_id != null;
                    mp.put("email_id",email_id);
                    mp.put("police_name",name.getText().toString().trim());
                    mp.put("designation",designation.getSelectedItem().toString().trim());
                    mp.put("policeStationId",policeStationID.getSelectedItem().toString().trim());
                    mp.put("posted_city",act.getText().toString().trim());
                    mp.put("rating","2");
                    int id = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(id);
                    mp.put("psHead",radioButton.getText().toString());

                    if(policeName.equals(""))
                    {
                        flag=1;
                        name.setError("Enter Your Name");
                    }else{
                        name.setError(null);
                    }
                    if(designation.getSelectedItem().toString().equals("Select Your Designation"))
                    {
                ((TextView)designation.getSelectedView()).setError("Plz Select Your Designation");
                        flag=1;
                    }else{
                ((TextView)designation.getSelectedView()).setError(null);
                    }
                    if(policeStationID.getSelectedItem().toString().equals("Select Police Station"))
                    {
                         ((TextView)policeStationID.getSelectedView()).setError("Plz Select Your Police Station");
                        flag=1;
                    }else{
                         ((TextView)policeStationID.getSelectedView()).setError(null);
                    }
                    if(listCity.contains(act.getText().toString()))
                    {
                        act.setError(null);
                    }else{
                        flag=1;
                        act.setError("Enter Valid City");
                    }
                    if(flag==0)
                    {
                        mProgressDialog.setTitle("Uploading");
                        mProgressDialog.setMessage("Please wait while we upload your data.");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                        myRef.child(FirebaseAuth.getInstance().getUid()).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressDialog.hide();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                    }



                }


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
                // add to the lists in the specified format
                listCity.add(city);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
