package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    Spinner spinner;
    private Button addCriminal;
    private TextView criminalAddress,crimeAddress,criminalName,bodyMark,crimeRating,crimeType;
    private ProgressDialog mProgressDialog;
    private RecyclerView criminalListRV;
    private RecyclerView.Adapter<CriminalListAdapter.ViewHolder> mAdapter;
    private ArrayList<Criminals> shortlistedCriminals;
    private Context ctx;
    private DatabaseReference mRootRef,mCriminalRef;
    private int error = 0;
    private  String getImageUri="";
    private AlertDialog alertDialog;

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
                if(error==1)
                {

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

}
