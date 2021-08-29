package com.example.tesutbk.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSubSoal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahSubsoal extends AppCompatActivity {
    String soal;
    String jenissoal;

    EditText no;
    EditText pertanyaan;
    EditText a;
    EditText b;
    EditText c;
    EditText d;
    EditText benar;

    private FirebaseAuth mAuth;
    ProgressDialog loading;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("subsoal");
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_subsoal);
        cekkoneksi();
        Intent data = getIntent();
        soal=data.getStringExtra("soal");
        jenissoal=data.getStringExtra("jenissoal");
        mAuth = FirebaseAuth.getInstance();

        TextView judul = findViewById(R.id.judulsub);
        judul.setText(soal);

        no = findViewById(R.id.no);
        pertanyaan = findViewById(R.id.pertanyaan);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        benar = findViewById(R.id.benar);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(TambahSubsoal.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();





        Button tambah = findViewById(R.id.tambahpertanyaan);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sno = no.getText().toString();
                String spertanyaan = pertanyaan.getText().toString();
                String sa = a.getText().toString();
                String sb = b.getText().toString();
                String sc = c.getText().toString();
                String sd = d.getText().toString();
                String sbenar = benar.getText().toString();


                if (sno.equals("")){
                    no.requestFocus();
                    no.setError("tidak boleh kosong");
                }else if (spertanyaan.equals("")){
                    pertanyaan.requestFocus();
                    pertanyaan.setError("tidak boleh kosong");
                }else if (sa.equals("")){
                    a.requestFocus();
                    a.setError("tidak boleh kosong");
                }else if (sb.equals("")){
                    b.requestFocus();
                    b.setError("tidak boleh kosong");
                }else if (sc.equals("")){
                    c.requestFocus();
                    c.setError("tidak boleh kosong");
                }else if (sd.equals("")){
                    d.requestFocus();
                    d.setError("tidak boleh kosong");
                }else if (sbenar.equals("")){
                    benar.requestFocus();
                    benar.setError("tidak boleh kosong");
                }else {
                loading = ProgressDialog.show(TambahSubsoal.this,null,"mohon tunggu",true,false);
                    ReqSubSoal reqSubSoal = new ReqSubSoal(sno,spertanyaan,sa,sb,sc,sd,sbenar);
                    database.child(jenissoal).child(soal).child(sno).setValue(reqSubSoal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loading.dismiss();


                            InterstitialAd.load(TambahSubsoal.this,"ca-app-pub-8529683226102901/7639229361", adRequest, new InterstitialAdLoadCallback() {
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
                                    Toast.makeText(getApplicationContext(),"gagal iklan",Toast.LENGTH_SHORT).show();
                                    mInterstitialAd = null;
                                }
                            });

                            //end ads
                            Toast.makeText(getApplicationContext(),"berhasil ditambahkan",Toast.LENGTH_SHORT).show();
                            if (jenissoal.equals("SAINTEK")){
                                Intent goback= new Intent(getApplicationContext() , Subsoal.class);
                                goback.putExtra("soal",soal);
                                goback.putExtra("jenissoal",jenissoal);
                                goback.putExtra("context","Saintek");
                                startActivity(goback);
                                finish();
                            }else if (jenissoal.equals("SOSHUM")){
                                Intent goback= new Intent(getApplicationContext() , Subsoal.class);
                                goback.putExtra("soal",soal);
                                goback.putExtra("jenissoal",jenissoal);
                                goback.putExtra("context","Soshum");
                                startActivity(goback);
                                finish();
                            }

                        }
                    });


                }

            }
        });
    }

    private void iklan() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(TambahSubsoal.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }


    //klo pencet kembali
    @Override
    public void onBackPressed() {
        if (jenissoal.equals("SAINTEK")){
            Intent goback= new Intent(getApplicationContext() , Subsoal.class);
            goback.putExtra("soal",soal);
            goback.putExtra("jenissoal",jenissoal);
            goback.putExtra("context","Saintek");
            startActivity(goback);
            finish();
        }else if (jenissoal.equals("SOSHUM")){
            Intent goback= new Intent(getApplicationContext() , Subsoal.class);
            goback.putExtra("soal",soal);
            goback.putExtra("jenissoal",jenissoal);
            goback.putExtra("context","Soshum");
            startActivity(goback);
            finish();
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
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Login Dulu Dong",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();

        }
    }
    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(getApplicationContext(), NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }



}