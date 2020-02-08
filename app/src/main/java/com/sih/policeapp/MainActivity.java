package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.sih.Utils.CompareImage;
import com.sih.policeapp.Activities.Beats;
import com.sih.policeapp.Activities.Weather;
import com.sih.policeapp.Activities.wanted_activity;
import com.sih.policeapp.Activities.Login;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    View headerView;
    FirebaseAuth.AuthStateListener authStateListener;
    String userno;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    DatabaseReference mRootRef;
    String policeid;
    private SensorManager sensorManager;
    TextView name, email;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("921956751866-jg40qt8pf61p1sn8luifhfg4711i9jfp.apps.googleusercontent.com")
                .requestEmail()
                .build();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.e("ak47", "on Start");
        super.onStart();
        Log.e("ak47", "on Start after super");
        mAuth.addAuthStateListener(authStateListener);
        Log.e("ak47", "on Start Ends");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.e("ak47", "user null");
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        else {
            policeid=FirebaseAuth.getInstance().getUid();
            Log.e("ak47", "not null");

        }
authStateListener=new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            Log.e("ak47", "user null");
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        else {
            policeid=FirebaseAuth.getInstance().getUid();
            Log.e("ak47", "not null");
        }


    }
};


        OneSignal.startInit(this)


                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();


        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Map<String,String> mp=new HashMap<>() ;

                FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(policeid).child("Notification").setValue(userId);
                FirebaseDatabase.getInstance().getReference().child("PoliceNotif").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).setValue(userId);

            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);



        setUpToolBar();
        mRootRef = FirebaseDatabase.getInstance().getReference();
       // mRootRef.child("Anshaj").child("asdf").setValue("asdfghjkl");
        navigationView = findViewById(R.id.clickable_menu);
        headerView = navigationView.inflateHeaderView(R.layout.header);
        name=  headerView.findViewById(R.id.polname);
        email=headerView.findViewById(R.id.polid);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.fir){
                    startActivity(new Intent(MainActivity.this, Beats.class));
                }
                if(menuItem.getItemId() == R.id.add_crime){
                    Intent intent = new Intent(MainActivity.this,AddCriminalActivity.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.wanted_list) {
                    Intent intent = new Intent(MainActivity.this, wanted_activity.class);
                    startActivity(intent);

                }
                if (menuItem.getItemId() == R.id.weathe) {
                   // picker();
                    Intent intent = new Intent(MainActivity.this, Weather.class);
                    startActivity(intent);

                }
                if (menuItem.getItemId() == R.id.updatewantedfiles) {
                    updateWanted();
                }
                if(menuItem.getItemId() == R.id.profile)
                {
                    Intent intent = new Intent(MainActivity.this, PoliceProfile.class);
                    intent.putExtra("user_id",FirebaseAuth.getInstance().getUid());
                    startActivity(intent);
                }
                if(menuItem.getItemId() == R.id.search){
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(MainActivity.this);
                }
                if (menuItem.getItemId() == R.id.logout) {

                    mAuth.signOut();
                    OneSignal.setSubscription(false);
                }
                return false;
            }
        });

    }

    private final static int AUTOCOMPLETE_REQUEST_CODE = 999;

   /* public void picker() {
        Places.initialize(getApplicationContext(), "AIzaSyBueo4gPZC7mDkWceuBzlDX19X_anAavLI");
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, placeFields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

       *//* PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        // for activty

        try {
            startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }*//*

    }

*/


    public void updateWanted()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("criminal_ref");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String fileName=ds.getKey()+".jpg";

                    try {
                        final File sdCard = Environment.getExternalStorageDirectory();
                        File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/Pictures/"+fileName);
                        if(!file.exists()) {

                            Log.d("pathssss", "onPictureTaken - wrote to ");


                            File dir = new File(sdCard.getAbsolutePath() + "/HashContact" + "/Pictures");
                            dir.mkdirs();
                            long t = System.currentTimeMillis();



                            Log.d("pathsssss", "onPictureTaken - wrote to " + fileName);

                            File outFile = new File(dir, fileName);
                            Log.d("pathsssss", "onPictureTaken - wrote to " + fileName + dir);
                            URL url = null;
                            try {
                                url = new URL(ds.child("profile_pic_url").getValue(String.class));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, "Download Started", Toast.LENGTH_SHORT).show();

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(url)));
                            request.setDescription("Downloading");
                            request.setTitle(fileName);
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Log.d("bhaiwa",Environment.DIRECTORY_DOWNLOADS);
                            request.setDestinationInExternalPublicDir( "HashContact/Pictures", fileName);

                            DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                            final long downloadID = manager.enqueue(request);
                            Log.d("downid", String.valueOf(downloadID));

                            BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    //Fetching the download id received with the broadcast
                                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);



                                }

                            };
                            registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                            Log.d("pathssss", "onPictureTaken - wrote to " + outFile.getAbsolutePath());
                        }


                    } finally {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);

            a_builder.setMessage("Do you want to Close this App !!!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }) ;
            AlertDialog alert = a_builder.create();
            alert.setTitle("Alert !!!");
            alert.show();

        }

    }
    int index=0;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " +place.getLatLng());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Uri imageUri = data.getData();
                FileOutputStream outStream = null;
                try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    long t = System.currentTimeMillis();
                    String time = String.valueOf(t);
                        final File sdCard = Environment.getExternalStorageDirectory();
                        //File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/ComparedPhoto/"+time+".jpg");

                            //Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.chatback);
                            Log.d("pathssss", "onPictureTaken - wrote to ");


                            File dir = new File(sdCard.getAbsolutePath() + "/HashContact" + "/ComparedPhoto");
                            dir.mkdirs();

                            String fileName = time+".jpg";
                            fileName.trim();
                          //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            Log.d("pathsssss", "onPictureTaken - wrote to " + fileName);

                            File outFile = new File(dir, fileName);
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
                    outStream.flush();
                    outStream.close();
                            Log.d("pathsssss", "onPictureTaken - wrote to " + fileName + dir);
                    File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/ComparedPhoto/"+fileName);
                   // File sdCardRoot = Environment.getExternalStorageDirectory();

                    File yourDir = new File(sdCard.getAbsolutePath() + "/HashContact" + "/Pictures");
                    long fileno=yourDir.listFiles().length;
                    long curr=1;
                    for (File f : yourDir.listFiles()) {
                        if (f.isFile())
                        {


                            CompareImage example=new CompareImage(this,file,f,fileno);



                                example.execute();



                        }
                        // Do your stuff
                    }


                } catch (IOException e) {
                    Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void setUpToolBar()
    {
        mDrawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

    }


}
