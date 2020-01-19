package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        setUpToolBar();
        navigationView = findViewById(R.id.clickable_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.fir){
                    Toast.makeText(MainActivity.this, "FIR", Toast.LENGTH_SHORT).show();
                }
                if(menuItem.getItemId() == R.id.add_crime){
                    Toast.makeText(MainActivity.this, "ADD CRIME", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

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
