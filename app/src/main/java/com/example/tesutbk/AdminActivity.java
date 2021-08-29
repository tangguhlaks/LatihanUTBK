package com.example.tesutbk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesutbk.admin.DatasoalActivity;
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

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private InterstitialAd mInterstitialAd;
    private AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        cekkoneksi();
        mAuth = FirebaseAuth.getInstance();

        //ads
        // interstial
       MobileAds.initialize(this, new OnInitializationCompleteListener() {
           @Override
           public void onInitializationComplete(InitializationStatus initializationStatus) {

           }
       });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(AdminActivity.this,"ca-app-pub-8529683226102901/7639229361", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                iklan();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Toast.makeText(getApplicationContext(),loadAdError.getMessage(),Toast.LENGTH_SHORT).show();
                mInterstitialAd = null;
            }
        });


        //bannerads
        mAdview = findViewById(R.id.adView);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        mAdview.loadAd(adRequestbanner);


        //data user
        CardView datauser = findViewById(R.id.datauser);
        datauser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,DatauserActivity.class);
                startActivity(i);
                finish();
            }
        });

        //data soal
        CardView datasoal = findViewById(R.id.datasoal);
        datasoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DatasoalActivity.class);
                startActivity(i);
                finish();
            }
        });

         //logout
        CardView keluar = findViewById(R.id.keluar);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent i = new Intent(AdminActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(AdminActivity.this, "Sampai Jumpa",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void iklan() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(AdminActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    //cek koneksi dan user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (!user.getEmail().equals("tangguhlaksana0@gmail.com")){
                Intent i = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(AdminActivity.this, "Login Dulu Dong",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AdminActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
    }
    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(AdminActivity.this,NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }


}