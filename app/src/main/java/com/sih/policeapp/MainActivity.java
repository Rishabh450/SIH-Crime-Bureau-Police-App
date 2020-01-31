package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.policeapp.Activities.Beats;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Anshaj").child("asdf").setValue("asdfghjkl");
        navigationView = findViewById(R.id.clickable_menu);
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
