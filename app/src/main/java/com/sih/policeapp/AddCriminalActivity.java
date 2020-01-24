package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCriminalActivity extends AppCompatActivity {

    private static final String TAG = "AddCriminalActivity";
    private FloatingActionButton add_criminal;
    private ImageView img_DOB,img_DOC;
    private EditText DOB,DOC;
    private int c=0;

    String imageUri;
    CircleImageView circleImageView;

    AutoCompleteTextView act,state,dist;
    Spinner spinner;
    private Button addCriminal;
    private TextView criminalAddress,crimeAddress,criminalName,bodyMark,crimeRating,crimeType;
    private ProgressDialog mProgressDialog;
    private RecyclerView criminalListRV;
    private RecyclerView.Adapter<CriminalListAdapter.ViewHolder> mAdapter;
    private ArrayList<Criminals> shortlistedCriminals;
    private Context ctx;
    private DatabaseReference mRootRef,mCriminalRef;
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







        add_criminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crop image activity api uses....
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddCriminalActivity.this);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                Log.i("asdfg1","i am here");
//                Log.i("asdfg1",resultUri.toString());
//
                Intent intent = new Intent(AddCriminalActivity.this, AddNewCriminalDetails.class);
                intent.putExtra("image", resultUri.toString());
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
