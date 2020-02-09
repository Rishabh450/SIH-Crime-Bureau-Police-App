package com.sih.policeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.policeapp.Activities.Login;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               sendToMainActicity();
            }
        }, 1000);

    }

    private void sendToMainActicity() {
//        mProgress.dismiss();
        DatabaseReference mRootRef ;
        mRootRef = FirebaseDatabase.getInstance().getReference().child("PoliceUser");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            mRootRef.child(FirebaseAuth.getInstance().getUid()).child("designation").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            startActivity(new Intent(SplashScreen.this, Login.class));
            finish();
        }






    }

}
