package com.zappfoundry.aermenu_v20;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class SplashScreenHandlerActivity extends AppCompatActivity {
   public static String x="BBR"; Context context;  public static String restName="A";public static String imageURL="A";
    private FirebaseAuth mAuth;public static String userName=""; public static String userEmail="";
    public static int categoryCount=0;
    public static int tokenID=1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient mgoogleSignInClient;
   public static String y="takeaway";
    private int shortAnimationDuration;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


                signIn();
            }
        }, 3000);
        mAuth = FirebaseAuth.getInstance();

        //setContentView(R.layout.activity_signin);
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
final ImageView logoRest=findViewById(R.id.imageView);
        mgoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        Uri uri = getIntent().getData();
final TextView x1=findViewById(R.id.textView38);
        TextView y1=findViewById(R.id.textView37);

        if (uri != null) {
            x = uri.getQueryParameter("x"); // x = "1.2"
            y = uri.getQueryParameter("y"); // y = "3.4"
        }
        if (x != null && y != null) {
            // do something interesting with x and y
            Log.e("x",x);
            if(x=="bigbitesburgers")
            {
                setTheme(R.style.AppThemeBBR);
                setContentView(R.layout.menu_display);
            }
            if(x=="cnc")
            {

                setTheme(R.style.AppThemeCNC);
                setContentView(R.layout.menu_display);
            }
          //  x1.setText(x);y1.setText(y);
            Log.e("y",y);
        }
        final boolean[] isFirstTime = {true};
        final DocumentReference docRefDesserts = db.collection(x.toUpperCase()).document("menu") ;
        docRefDesserts.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot snapshot,
                                @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    {

                        Picasso.get().load((snapshot.get("logoImage").toString())).into(logoRest);
                        restName=(snapshot.get("restaurantName").toString());
                        imageURL=(snapshot.get("logoImage").toString());
                        x1.setText((snapshot.get("restaurantName").toString()));
                        categoryCount=Integer.parseInt(String.valueOf(snapshot.get("categoryCount")));
                        if(isFirstTime[0]){
                        try{
                        tokenID=Integer.parseInt(String.valueOf(snapshot.get("tokenNumber")));
                        if(tokenID<20){docRefDesserts.update("tokenNumber", ++tokenID);}else{docRefDesserts.update("tokenNumber", 1);}
                        isFirstTime[0] =false;
                        }catch (Exception b){}}

                        Log.e("categoryCount", String.valueOf(categoryCount));
                    /*    List<Map<String, Object>> veg = (List<Map<String, Object>>)  snapshot.get("veg");
                        for(int i=0;i<veg.size();i++){
                            Map<String, Object> m=veg.get(i);
                            Log.e("menuItem",m.toString());
                            MenuItem item = new MenuItem();

                        }*/


                    }
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });

    //    Picasso.get().load(menuItemListDrinks.get(rvHolder.getAdapterPosition()).getImageURL().toString()).into(itemImage);
    }
    protected void onActivityResult(int requestCode, int Resultcode, @Nullable Intent data)

    {

        super.onActivityResult(requestCode, Resultcode, data);
        if(requestCode==1){
            Task<GoogleSignInAccount> task= com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    void handleSignInResult(Task<GoogleSignInAccount>  completedtask){
        try{
            GoogleSignInAccount googleSignInAccount=completedtask.getResult(ApiException.class);
            Toast.makeText(SplashScreenHandlerActivity.this,"Success",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(googleSignInAccount);
        }catch (Exception ent){   Log.e("exx", String.valueOf(ent));Toast.makeText(SplashScreenHandlerActivity.this,"Fail",Toast.LENGTH_LONG).show();}
    }

    void FirebaseGoogleAuth(final GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(SplashScreenHandlerActivity.this,"Success",Toast.LENGTH_LONG).show();
                    userName=googleSignInAccount.getDisplayName();
                    if(userName.contains(" ")){
                        userName= userName.substring(0, userName.indexOf(" "));
                    }
                    userEmail=googleSignInAccount.getEmail();
                    Intent i = new Intent(SplashScreenHandlerActivity.this, MenuDisplay.class);
                    i.putExtra("x",x);
                    startActivity(i);
                }
                else{
                    Toast.makeText(SplashScreenHandlerActivity.this,"Fail 2",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void signIn()
    {
        Intent intent=mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,1);
    }
}

