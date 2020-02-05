package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.sih.Utils.CompareImage;
import com.sih.policeapp.Activities.Beats;
import com.sih.policeapp.Activities.Login;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;View headerView;
    FirebaseAuth.AuthStateListener authStateListener;String userno;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    DatabaseReference mRootRef;
    String policeid;private SensorManager sensorManager;
    TextView name,email;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("921956751866-jg40qt8pf61p1sn8luifhfg4711i9jfp.apps.googleusercontent.com")
                .requestEmail()
                .build();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.e("ak47","on Start");
        super.onStart();
        Log.e("ak47","on Start after super");
        mAuth.addAuthStateListener(authStateListener);
        Log.e("ak47","on Start Ends");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
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
                mp.put("Notification",userId);
                mp.put("Name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


                FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(policeid).setValue(mp);
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
                    Toast.makeText(MainActivity.this, "FIR", Toast.LENGTH_SHORT).show();
                }
                if(menuItem.getItemId() == R.id.add_crime){
                    Intent intent = new Intent(MainActivity.this,AddCriminalActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId() == R.id.search){
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(MainActivity.this);
                }
                if(menuItem.getItemId() == R.id.logout){

                    mAuth.signOut();
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
    int index=0;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    for (File f : yourDir.listFiles()) {
                        if (f.isFile())
                        {

                            CompareImage example=new CompareImage(this,file,f);
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
