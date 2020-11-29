package com.zappfoundry.aermenu_v20;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignIn extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    GoogleSignInClient mgoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    String name, email;
    private int shortAnimationDuration;
    String idToken;
    Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(GoogleSignIn.this,"GSignAct",Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_splash);
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        final ImageView img = findViewById(R.id.imageView5);
        final TextView AermenuName = findViewById(R.id.textView13);
        img.setAlpha(0f);
        img.setVisibility(View.VISIBLE);
        AermenuName.setAlpha(0f);
        AermenuName.setVisibility(View.VISIBLE);
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
        // Animate the content view to 100% opacity, and clear any animation
        // l//istener set on the view.
        img.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        AermenuName.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                img.animate()
                        .alpha(0f)
                        .setDuration(shortAnimationDuration)
                        .setListener(null);
                AermenuName.animate()
                        .alpha(0f)
                        .setDuration(shortAnimationDuration)
                        .setListener(null);
            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
              /*  Intent i = new Intent(SplashScreenHandlerActivity.this, GoogleSignIn.class);
                startActivity(i);*/
signIn();/*
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
         */       // finish();

            }
        }, 3000);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //setContentView(R.layout.activity_signin);
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);

    }
void signIn()
{
    Intent intent=mgoogleSignInClient.getSignInIntent();
    startActivityForResult(intent,1);
}


protected void onActivityResult(int requestCode, int Resultcode, @Nullable Intent data) {

    super.onActivityResult(requestCode, Resultcode, data);
    if(requestCode==1){
        Task<GoogleSignInAccount> task= com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);
    }
}

void handleSignInResult(Task<GoogleSignInAccount>  completedtask){
        try{
            GoogleSignInAccount googleSignInAccount=completedtask.getResult(ApiException.class);
            Toast.makeText(GoogleSignIn.this,"Success",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(googleSignInAccount);
        }catch (Exception e){ Toast.makeText(GoogleSignIn.this,"Fail",Toast.LENGTH_LONG).show();}
}

void FirebaseGoogleAuth(GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential=GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(GoogleSignIn.this,"Success",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(GoogleSignIn.this, MenuDisplay.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(GoogleSignIn.this,"Fail",Toast.LENGTH_LONG).show();
                }
            }
        });
}
}




























