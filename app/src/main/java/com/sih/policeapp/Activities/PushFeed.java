package com.sih.policeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sih.policeapp.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushFeed extends AppCompatActivity {
    EditText feedContent;
    ImageView feedImage;
    Button addimage;
    Button upload;
    Button crop;
    LinearLayout shareFb;
    LinearLayout shareInsta;
    String upload_text;
    Uri pickedImage;
    CardView feedcard;
    ArrayList<Map<String,String>> post=new ArrayList<>();
    String provider,currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_feed);
        crop=findViewById(R.id.cropimage);
        init();
        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();

        currentUser=userInfo.getUid();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickedImage!=null) {
                    // com.example.litereria.Support.Post post=new com.example.litereria.Support.Post();
                    CropImage.activity(pickedImage)

                            .start(PushFeed.this);
                }
                else
                    Toast.makeText(PushFeed.this,"Add image first",Toast.LENGTH_SHORT).show();


            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(PushFeed.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    // Log.e(TAG, "setxml: peremission prob");
                    ActivityCompat.requestPermissions(PushFeed.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);


                }
                else
                    addImage();

            }
        });
    }
    final int PIC_CROP = 1;

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(PushFeed.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void init()
    {
        feedContent= findViewById(R.id.feed_upload_content);
        feedImage=findViewById(R.id.post_image_display);
        addimage=findViewById(R.id.add_image_to_post);
        upload=findViewById(R.id.upload_feed);
        feedcard=findViewById(R.id.feedcard);


    }
    public void addImage()
    {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, 21);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                pickedImage=data.getData();
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                feedImage.setImageBitmap(selectedBitmap);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("cropmeghusa","ghusa");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                pickedImage = result.getUri();

                Toast.makeText(PushFeed.this,"Image cropped successfully in background,upload now",Toast.LENGTH_SHORT).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        else if (resultCode == RESULT_OK&&requestCode==21) {

            pickedImage = data.getData();

            Log.d("successhuacrop","addhua"+pickedImage);

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Log.d("hey yo",imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
            feedcard.setVisibility(View.VISIBLE);
            feedImage.setImageDrawable(mDrawable);


            cursor.close();
        }
    }
    public void upload()
    {   if(pickedImage!=null) {
        final long ts = (long) System.currentTimeMillis();
        final FirebaseDatabase[] database1 = {FirebaseDatabase.getInstance()};
        final DatabaseReference databaseReference1 = database1[0].getReference();
        final String mediaId = String.valueOf(ts);
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("FeedImage/").child(String.valueOf(ts));
        final UploadTask uploadTask;
        Bitmap bitmap = null;
        try {


            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pickedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] dat = baos.toByteArray();
        Log.d("kamwa kiya", "true");
        uploadTask = filePath.putBytes(dat);

        // uploadTask = filePath.putFile(Uri.parse(selectedImage));
        Log.d("sender", String.valueOf(pickedImage));
        final ProgressDialog mProgress = new ProgressDialog(PushFeed.this);
        mProgress.setTitle("Uploading...");


        mProgress.setCancelable(true);

        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadTask.cancel();
            }
        });

        //  mProgress.setInverseBackgroundForced(setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY));
        mProgress.show();


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String url = String.valueOf(uri);

                        final Map<String,String> map=new HashMap<>();

                        // databaseReference1.child("Feeds").child(String.valueOf(ts)).child("imageURL").setValue(url);
                        map.put("imageURL",url);
                        // post.add(map);
                        // map.clear();
                        map.put("subEvent","");
                        // post.add(map);
                        //  map.clear();

                        // databaseReference1.child("Feeds").child(String.valueOf(ts)).child("subEvent").setValue("");
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(currentUser).child("profile_pic");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String profilePictureUri1 = dataSnapshot.getValue(String.class);

                                final Map<String,String> map=new HashMap<>();

                                // databaseReference1.child("Feeds").child(String.valueOf(ts)).child("imageURL").setValue(url);
                                map.put("imageURL",url);
                                // post.add(map);
                                // map.clear();
                                map.put("subEvent","");
                                // post.add(map);
                                // Map<String,String> map1=new HashMap<>();
                                map.put("senderURL",profilePictureUri1);
                                // post.add(map1);
                                DatabaseReference ref = databaseReference1.child("PoliceUser").child(currentUser).child("police_name");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String profilePictureUri2 = dataSnapshot.getValue(String.class);
                                        //  databaseReference1.child("Feeds").child(String.valueOf(ts)).child("event").setValue(profilePictureUri1);
                                        // Map<String,String> map2=new HashMap<>();
                                        map.put("event",profilePictureUri2);
                                        //post.add(map2);
                                        //  map2.clear();
                                        map.put("comments","");
                                        // post.add(map2);
                                        // map2.clear();
                                        map.put("likes","");
                                        //  post.add(map2);
                                        //  map2.clear();
                                        map.put("senderID",currentUser);
                                        // post.add(map2);
                                        // map2.clear();
                                        if(feedContent.getText().toString().equals(""))
                                            map.put("content","");
                                        else

                                            map.put("content",feedContent.getText().toString());
                                        //  post.add(map2);

                                        databaseReference1.child("Feeds").child(String.valueOf(ts)).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mProgress.dismiss();
                                                Toast.makeText(PushFeed.this, "Uploaded", Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                // databaseReference1.child("Feeds").child(String.valueOf(ts)).child("senderURL").setValue(profilePictureUri1);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        //  databaseReference1.child("Feeds").child(String.valueOf(ts)).child("comments").setValue("");

                        //databaseReference1.child("Feeds").child(String.valueOf(ts)).child("likes").setValue("");
                        //  databaseReference1.child("Feeds").child(String.valueOf(ts)).child("senderID").setValue(currentUser);


                       /* if (!feedContent.getText().toString().equals(""))
                            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("content").setValue(feedContent.getText().toString());

                        else
                            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("content").setValue("");
*/



                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                mProgress.setMessage("Uploaded: " + (int) progress + "%");
                mProgress.setProgress((int) progress);

            }
        });
    }
    else{
        if(feedContent.getText().toString().equals(""))
            Toast.makeText(PushFeed.this,"No content to post",Toast.LENGTH_SHORT).show();
        else
        {
            final long ts = (long) System.currentTimeMillis();
            final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("comments").setValue("");
            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("senderID").setValue(currentUser);





            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("imageURL").setValue("");
            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("subEvent").setValue("");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(currentUser).child("profile_pic");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String profilePictureUri1 = dataSnapshot.getValue(String.class);
                    databaseReference1.child("Feeds").child(String.valueOf(ts)).child("senderURL").setValue(profilePictureUri1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference ref = databaseReference1.child("PoliceUser").child(currentUser).child("police_name");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String profilePictureUri1 = dataSnapshot.getValue(String.class);
                    databaseReference1.child("Feeds").child(String.valueOf(ts)).child("event").setValue(profilePictureUri1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("senderID").setValue(currentUser);


            databaseReference1.child("Feeds").child(String.valueOf(ts)).child("likes").setValue("");


            if (!feedContent.getText().toString().equals(""))
                databaseReference1.child("Feeds").child(String.valueOf(ts)).child("content").setValue(feedContent.getText().toString());



            Toast.makeText(PushFeed.this, "Uploaded", Toast.LENGTH_LONG).show();


        }
    }
    }
    public  Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;


        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;

    }
}
