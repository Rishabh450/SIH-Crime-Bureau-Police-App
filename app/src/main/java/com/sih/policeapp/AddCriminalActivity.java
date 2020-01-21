package com.sih.policeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddCriminalActivity extends AppCompatActivity {

    private static final String TAG = "AddCriminalActivity";
    private FloatingActionButton add_criminal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_criminal);
        add_criminal = findViewById(R.id.add_criminal);
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
