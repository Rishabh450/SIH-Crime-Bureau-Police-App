package com.sih.policeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.nio.charset.StandardCharsets.*;

public class AddNewCriminalDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AddNewCriminalDetails";
    private EditText DOB,DOC;
    private int c=0;

    String imageUri;
    CircleImageView circleImageView;
    // array lists
    // for the spinner in the format : City_no : City , State. Eg : 144 : New Delhi , India
    ArrayList<String> listSpinner=new ArrayList<String>();
    // to store the city and state in the format : City , State. Eg: New Delhi , India
    ArrayList<String> listAll=new ArrayList<String>();
    // for listing all states
    ArrayList<String> listState=new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity=new ArrayList<String>();
    // access all auto complete text views

    Map<String , ArrayList<String>> stateDistrict;
    Map<String , ArrayList<String>> districtLocality = new HashMap<>();
    Map<String , Long > localityPincode = new HashMap<>();
    private ImageView img_DOB,img_DOC;

    AutoCompleteTextView act,state,dist;
    Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_criminal_details);
        circleImageView = findViewById(R.id.new_criminal_pic);
        stateDistrict = new HashMap<>();
        state = findViewById(R.id.state);
        DOB=findViewById(R.id.DOB);
        DOC = findViewById(R.id.DOC);
        img_DOB = findViewById(R.id.img_DOB);
        img_DOC=findViewById(R.id.img_DOC);
        dist = findViewById(R.id.district);

        imageUri = getIntent().getStringExtra("image");
        Picasso.with(getApplicationContext())
                .load(imageUri)
                .into(circleImageView);
       // spinner=findViewById(R.id.spCity);
        callAll();
        setOnClicks();


        }
    public void callAll()
    {
        obj_list();
        addState();
    }
    private void setOnClicks()
    {
        img_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c=0;
                showDatePickerDialog();
            }
        });
        img_DOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c=1;
                showDatePickerDialog();
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
                listSpinner.add(String.valueOf(i+1)+" : "+city+" , "+state);
                listAll.add(city+" , "+state);
                listCity.add(city);
                listState.add(state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    void dist_list(String s)
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            listCity.clear();
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                if(state.equals(s))
                listCity.add(city);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    // The third auto complete text view
    void addState()
    {
        Set<String> set = new HashSet<String>(listState);
        act=(AutoCompleteTextView)findViewById(R.id.state);
        adapterSetting(new ArrayList(set));
    }

    // setting adapter for auto complete text views
    void adapterSetting(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        act.setAdapter(adapter);
        hideKeyBoard();
    }

    // hide keyboard on selecting a suggestion
    public void hideKeyBoard()
    {
            act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    dist_list(state.getText().toString());
                    Log.i(TAG, "onItemClick: "+ act.getText().toString());
                    act=findViewById(R.id.district);
                    adapterSetting(listCity);
                }
            });


    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date_of_month) {
        String day,mon,date;
        month = month + 1;
        day = String.valueOf(date_of_month);
        mon = String.valueOf(month);
       int mYear = Calendar.getInstance().get(Calendar.YEAR);
        int mMonth     =   Calendar.getInstance().get(Calendar.MONTH);
        int m     =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        if(day.length()==1) day = "0"+day;
        if(mon.length()==1) mon = "0"+mon;
        date = day + "/" + mon + "/" + year ;
      if(c==0)
      {
          DOB.setText(date);
      }
        if(c==1)
        {
            DOC.setText(date);
        }
    }
}
