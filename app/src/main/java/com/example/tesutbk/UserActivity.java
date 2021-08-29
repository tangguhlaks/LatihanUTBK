package com.example.tesutbk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesutbk.model.Datauser;
import com.example.tesutbk.user.SoalUser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("user");

    private String username;
    private String email;

    ProgressDialog loading;
    private InterstitialAd mInterstitialAd;
    private AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        cekkoneksi();

        loading = ProgressDialog.show(UserActivity.this,null,"mohon tunggu",true,false);
        TextView hello = findViewById(R.id.hellouser);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dbUser.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Datauser datauser = snapshot.getValue(Datauser.class);
                hello.setText("Selamat Datang "+datauser.getUsername());
                username = datauser.getUsername();
                email = datauser.getEmail();
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(UserActivity.this,"ca-app-pub-8529683226102901/7639229361", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(UserActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Toast.makeText(getApplicationContext(),"gagal iklan",Toast.LENGTH_SHORT).show();
                mInterstitialAd = null;
            }
        });
        mAdview = findViewById(R.id.banneruser);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest1);




        //card sett
        CardView lainya = findViewById(R.id.lainyaui);
        lainya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Lainya.class);
                i.putExtra("username",username);
                i.putExtra("email",email);
                startActivity(i);
                finish();
            }
        });

        CardView saintek = findViewById(R.id.saintekui);
        saintek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SoalUser.class);
                i.putExtra("jenissoal","SAINTEK");
                startActivity(i);
                finish();
            }
        });
        CardView soshum = findViewById(R.id.soshumui);
        soshum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SoalUser.class);
                i.putExtra("jenissoal","SOSHUM");
                startActivity(i);
                finish();
            }
        });

    }


    //cek koneksi dan user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getEmail().equals("tangguhlaksana0@gmail.com")){
                Intent i = new Intent(UserActivity.this, AdminActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(UserActivity.this, "Login Dulu Dong",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UserActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
    }
    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(UserActivity.this,NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }

}