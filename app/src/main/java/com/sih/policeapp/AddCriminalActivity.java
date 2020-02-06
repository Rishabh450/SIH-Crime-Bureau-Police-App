package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class AddCriminalActivity extends AppCompatActivity {

    private static final String TAG = "AddCriminalActivity";
    private FloatingActionButton add_criminal;
    private ImageView img_DOB,img_DOC;
    private EditText DOB,DOC;
    private int c=0;

    String imageUri;
    CircleImageView circleImageView;

    AutoCompleteTextView act,state,dist;
    Spinner spinner,query_type;
    private Button addCriminal;
    private TextView criminalAddress,crimeAddress,criminalName,bodyMark,crimeRating,crimeType,title;
    private ProgressDialog mProgressDialog;
    private RecyclerView criminalListRV;
    private RecyclerView.Adapter<CriminalListAdapter.ViewHolder> mAdapter;
    private ArrayList<Criminals> shortlistedCriminals;
    private Context ctx;
    private DatabaseReference mRootRef,mCriminalRef;
    private int error = 0;
    private  String getImageUri="";
    private AlertDialog alertDialog;
    private ImageView search,cross,down,search2;
    private EditText searchEditext;
    private CardView sortCV,searchCV,noMatchesFoundCV;
    private ArrayAdapter<CharSequence> adapter;
    private String searchedWord="",searchType="";
    ArrayList<String> listState=new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity=new ArrayList<String>();
    // access all auto complete text views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_criminal);
        add_criminal = findViewById(R.id.add_criminal);
        DOB= findViewById(R.id.DOB);
        img_DOB = findViewById(R.id.img_DOB);
        mProgressDialog = new ProgressDialog(this);
        criminalAddress = findViewById(R.id.address_of_criminal);
        criminalName =findViewById(R.id.criminal_name);
        bodyMark = findViewById(R.id.body_mark);
        criminalListRV = findViewById(R.id.criminal_list_RV);
        shortlistedCriminals = new ArrayList<>();
        ctx = getApplicationContext();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCriminalRef = mRootRef.child("criminal_ref");
        search = findViewById(R.id.search);
        search2 = findViewById(R.id.search2);
        cross = findViewById(R.id.CROSS);
        title  = findViewById(R.id.title);
        down = findViewById(R.id.down);
        sortCV = findViewById(R.id.sort_cv);
        sortCV.setVisibility(View.GONE);
        searchCV = findViewById(R.id.searchCV);
        searchCV.setVisibility(View.GONE);
        searchEditext = findViewById(R.id.search_editText);
        spinner = findViewById(R.id.spinner);
        noMatchesFoundCV = findViewById(R.id.noMatchesFound);
        act=findViewById(R.id.stateOrDistrict);
        noMatchesFoundCV.setVisibility(View.GONE);
        query_type = findViewById(R.id.query_type);
        adapter = ArrayAdapter.createFromResource(this,R.array.QueryType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        query_type.setAdapter(adapter);
        obj_list();
        query_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1)
                {
                    searchCV.setVisibility(View.VISIBLE);
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.SearchBy,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(query_type.getSelectedItem().toString().equals("SearchBy"))
                            {
                                searchType = spinner.getSelectedItem().toString().trim();
                                if(i==0)
                                {
                                    Set<String> set = new HashSet<String>(listState);
                                    ArrayList arrayList = new ArrayList(set);
                                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,arrayList);
                                    act.setAdapter(adapter);
                                }
                                if(i==1)
                                {
                                    Set<String> set = new HashSet<String>(listCity);
                                    ArrayList arrayList = new ArrayList(listCity);

                                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,arrayList);
                                    act.setAdapter(adapter);
                                }
                            }
                            if(query_type.getSelectedItem().toString().equals("SortBy"))
                            {
                                sortBy(spinner.getSelectedItem().toString().trim());
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if(i==0)
                {
                    searchCV.setVisibility(View.GONE);
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.SortBy,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    if(query_type.getSelectedItem().toString().equals("SortBy"))
                    {
                        sortBy(spinner.getSelectedItem().toString().trim());
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Log.i(TAG, "onCreate: " + listState.toString());





        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String str = charSequence.toString().trim();
                String cap=str;
                if(str.length()>0)
                {
                     cap = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                }

                searchBy(searchType,cap);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(query_type.getSelectedItem().toString().equals("SortBy"))
                {
                    sortBy(spinner.getSelectedItem().toString().trim());
                }
                else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        setOnClicks();
        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Checking Face in Image....Please Wait.....")
                .build();

        mCriminalRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {

                    Criminals criminal = dataSnapshot.getValue(Criminals.class);
                    assert criminal != null;
                    {
                        LinearLayoutManager mLayoutManager;
                        mLayoutManager = new LinearLayoutManager(AddCriminalActivity.this);
                        mLayoutManager.setReverseLayout(true);
                        mLayoutManager.setStackFromEnd(true);

                        // And set it to RecyclerView
                        criminalListRV.setLayoutManager(mLayoutManager);
                        mAdapter = new CriminalListAdapter(ctx,shortlistedCriminals);
                        criminalListRV.setAdapter(mAdapter);
                        //  mAdapter.notifyDataSetChanged();
                        shortlistedCriminals.add(criminal);
                        mAdapter.notifyItemInserted(0);
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Criminals criminal = dataSnapshot.getValue(Criminals.class);
                assert criminal != null;
                {

                    if (shortlistedCriminals.contains(criminal))
                    {
                        int a = shortlistedCriminals.lastIndexOf(criminal);
                        mAdapter.notifyItemChanged(a);
                    }

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Criminals criminal = dataSnapshot.getValue(Criminals.class);
                assert criminal != null;
                {

                    if (shortlistedCriminals.contains(criminal))
                    {
                        int a = shortlistedCriminals.lastIndexOf(criminal);
                        shortlistedCriminals.remove(criminal);
                        mAdapter.notifyItemRemoved(a);
                    }

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setOnClicks() {
        add_criminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crop image activity api uses....
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddCriminalActivity.this);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               title.setVisibility(View.GONE);
               searchEditext.setVisibility(View.VISIBLE);
               search.setVisibility(View.GONE);
               cross.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditext, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = act.getText().toString().trim();

                searchByStateOrDistrict(act.getText().toString().trim());
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setVisibility(View.VISIBLE);
                searchEditext.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                cross.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditext.getWindowToken(), 0);
            }
        });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(criminalListRV.getLayoutParams());
//                    marginLayoutParams.setMargins(0, 80, 0, 0);
//                    criminalListRV.setLayoutParams(marginLayoutParams);
                    if(sortCV.getVisibility()==View.GONE)
                    {
                        down.setRotation(180);
                        search.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        searchEditext.setVisibility(View.GONE);
                        sortCV.setVisibility(View.VISIBLE);
                        if(query_type.getSelectedItem().toString().equals("SearchBy")){
                            searchCV.setVisibility(View.VISIBLE);
                        }

                    }
                    else{
                        down.setRotation(360);
                        search.setVisibility(View.VISIBLE);
                        title.setVisibility(View.VISIBLE);
                        sortCV.setVisibility(View.GONE);
                        searchCV.setVisibility(View.GONE);
                    }

                }
            });
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


    }

    private void searchByStateOrDistrict(final String s) {
        String s1 = "";
        Log.i(TAG, "searchByStateOrDistrict: " +   s);
        if(s.equals("")) ;
        else{
            if( !(listCity.contains(s) || listState.contains(s)) ) ;
             else if(spinner.getSelectedItem().toString().trim().equals("State of crime")) s1="state_of_crime";
             else if(spinner.getSelectedItem().toString().trim().equals("District/City of Crime"))  s1="district_of_crime";
         }
        if(!s1.equals(""))
        {
            Log.i(TAG, "searchByStateOrDistrict: " +   s);
            shortlistedCriminals.clear();
            final String finalS = s1;
            mRootRef.child("crime_ref").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable final String str) {
                    if(dataSnapshot.exists())
                    {
                        Crime crime = dataSnapshot.getValue(Crime.class);
                        assert crime != null;
                       // Log.i(TAG, "onChildAdded: " + finalS + " " + s + " " + crime.getState_of_crime() + " " + crime.getDistrict_of_crime());
                        if(finalS.equals("state_of_crime"))
                        {
                          //  Toast.makeText(AddCriminalActivity.this, "HERE", Toast.LENGTH_SHORT).show();
                            assert crime != null;
                            if(crime.getState_of_crime().equals(s))
                            {
                                mCriminalRef.child(crime.getCriminal_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Criminals criminals = dataSnapshot.getValue(Criminals.class);
                                        if(!shortlistedCriminals.contains(criminals))
                                        {

                                            shortlistedCriminals.add(criminals);
                                            mAdapter.notifyDataSetChanged();

                                        }
                                        if(shortlistedCriminals.size()==0) {
                                            mAdapter.notifyDataSetChanged();
                                            noMatchesFoundCV.setVisibility(View.VISIBLE);
                                        }else{
                                            noMatchesFoundCV.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                        else {
                            Log.i(TAG, "onChildAdded: " + finalS + " " + s + " " + crime.getState_of_crime() + " " + crime.getDistrict_of_crime());
                            assert crime != null;
                            if(crime.getDistrict_of_crime().equals(s))
                            {
                                mCriminalRef.child(crime.getCriminal_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Criminals criminals = dataSnapshot.getValue(Criminals.class);
                                        if(!shortlistedCriminals.contains(criminals))
                                        {

                                            shortlistedCriminals.add(criminals);
                                            mAdapter.notifyDataSetChanged();

                                        }
                                        if(shortlistedCriminals.size()==0) {
                                            mAdapter.notifyDataSetChanged();
                                            noMatchesFoundCV.setVisibility(View.VISIBLE);
                                        }else{
                                            noMatchesFoundCV.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    }
                    if(shortlistedCriminals.size()==0) {
                        mAdapter.notifyDataSetChanged();
                        noMatchesFoundCV.setVisibility(View.VISIBLE);
                    }else{
                        noMatchesFoundCV.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void searchBy(final String search_type, String s)
    {

        String str="";
        if(search_type.equals("")) str="criminal_name";
        if(search_type.equals("Criminal Name")) str="criminal_name";
        if(search_type.equals("District/City of Crime")) str="district_of_crime";
        if(search_type.equals("State of Crime")) str="state_of_crime";


        Query query1 = FirebaseDatabase.getInstance().getReference().child("crime_ref").child(str).startAt(s).endAt(s+"\uf8ff");

        Query query = FirebaseDatabase.getInstance().getReference().child("criminal_ref").orderByChild("criminal_name").startAt(s).endAt(s+"\uf8ff");
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(AddCriminalActivity.this);
        criminalListRV.setLayoutManager(mLayoutManager);
        shortlistedCriminals.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    {
                        shortlistedCriminals.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            Criminals criminals = dataSnapshot1.getValue(Criminals.class);
                            shortlistedCriminals.add(criminals);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
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
                // add to the lists in the specified format
                listCity.add(city);
                listState.add(state);
            }
            listState = removeDuplicates(listState);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }



    public void sortBy(String s)
    {
         if(s.equals("Random")) s="criminal_id";
        if(s.equals("Criminal Name")) s="criminal_name";
        if(s.equals("Criminal Rating")) s="criminal_rating";
        Query query = FirebaseDatabase.getInstance().getReference().child("criminal_ref")
                .orderByChild(s);
        shortlistedCriminals.clear();
       // Toast.makeText(ctx, "call", Toast.LENGTH_SHORT).show();
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(AddCriminalActivity.this);
        if( !s.equals("criminal_name"))
        {
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }


        // And set it to RecyclerView
        criminalListRV.setLayoutManager(mLayoutManager);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {

                    Criminals criminal = dataSnapshot.getValue(Criminals.class);
                    assert criminal != null;
                    {

                        mAdapter = new CriminalListAdapter(ctx,shortlistedCriminals);
                        criminalListRV.setAdapter(mAdapter);
                        //  mAdapter.notifyDataSetChanged();
                        shortlistedCriminals.add(criminal);
                        mAdapter.notifyItemInserted(0);
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Criminals criminal = dataSnapshot.getValue(Criminals.class);
                assert criminal != null;
                {

                    if (shortlistedCriminals.contains(criminal))
                    {
                        int a = shortlistedCriminals.lastIndexOf(criminal);
                        mAdapter.notifyItemChanged(a);
                    }

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Criminals criminal = dataSnapshot.getValue(Criminals.class);
                assert criminal != null;
                {

                    if (shortlistedCriminals.contains(criminal))
                    {
                        int a = shortlistedCriminals.lastIndexOf(criminal);
                        shortlistedCriminals.remove(criminal);
                        mAdapter.notifyItemRemoved(a);
                    }

                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Bitmap bitmap;
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Uri imageUri = data.getData();
                try {
                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    alertDialog.show();
                     processFaceDetection(bitmap);
                     getImageUri = resultUri.toString();

                } catch (IOException e) {
                    Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void processFaceDetection(Bitmap bitmap) {
        FirebaseVisionImage firebaseVisionImage  = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions.Builder().build();

        FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance().getVisionFaceDetector(firebaseVisionFaceDetectorOptions);

        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                getFaceResults(firebaseVisionFaces);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCriminalActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFaceResults(List<FirebaseVisionFace> firebaseVisionFaces) {
        int counter = 0;
        for( FirebaseVisionFace face : firebaseVisionFaces)
        {
            counter = counter + 1;
        }
        alertDialog.dismiss();
        if(counter==0)
        {
            error = 1;
            Toast.makeText(this, "Face Not Detected Take a clear pic", Toast.LENGTH_SHORT).show();
        }
        else if(counter>1)
        {
            error = 1;
            Toast.makeText(this, "More Than One Face" +
                    " Detected !!", Toast.LENGTH_SHORT).show();
        }
        else{

            Intent intent = new Intent(AddCriminalActivity.this, AddNewCriminalDetails.class);
            intent.putExtra("image", getImageUri);
            startActivity(intent);
        }
    }
    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }

}
