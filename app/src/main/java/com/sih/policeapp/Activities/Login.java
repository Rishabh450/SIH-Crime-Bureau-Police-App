package com.sih.policeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.policeapp.MainActivity;
import com.sih.policeapp.R;
import com.sih.policeapp.RegisterActivity;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    int RC_SIGN_IN=1;
    ConstraintLayout constraintLayout;
    ImageView imageView,image;
    private ProgressDialog mProgress;
    SignInButton signInButton;
    Button facebook_sign_in;
    FirebaseAuth.AuthStateListener mAuthstateListner;
    DatabaseReference mRootRef;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            sendToMainActicity();
        }
    }
    private void sendToMainActicity() {
//        mProgress.dismiss();
        constraintLayout=findViewById(R.id.login);
        constraintLayout.setVisibility(View.GONE);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("PoliceUser");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        mRootRef.child(FirebaseAuth.getInstance().getUid()).child("designation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(Login.this, RegisterActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        signInButton=findViewById(R.id.signin);






// ...
// Initialize Firebase Auth


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                signIn();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("921956751866-jg40qt8pf61p1sn8luifhfg4711i9jfp.apps.googleusercontent.com")
                .requestEmail()
                .build();
        Log.e("ak47","on Starting");
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // mCallbackManager.onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ak47", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("ak47", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ak47", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendToMainActicity();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ak47", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this,"Something wrong",Toast.LENGTH_LONG).show();
                            // updateUI(null);
                        }


                    }
                });
    }
}
