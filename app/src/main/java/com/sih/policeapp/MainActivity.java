package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.sih.policeapp.Activities.Beats;
import com.sih.policeapp.Activities.Login;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;View headerView;
    FirebaseAuth.AuthStateListener authStateListener;String userno;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    DatabaseReference mRootRef;
    String policeid;
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
                FirebaseDatabase.getInstance().getReference().child("PoliceUser").child(policeid).child("Notification").setValue(userId);
                FirebaseDatabase.getInstance().getReference().child("PoliceNotif").child(userId).setValue(userId);

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
