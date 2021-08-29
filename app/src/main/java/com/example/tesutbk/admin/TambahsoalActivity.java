package com.example.tesutbk.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSoal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahsoalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etsoal;
    private EditText etjenissoal;
    private EditText etstatussoal;
    private DatabaseReference database;
    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahsoal);
        mAuth=FirebaseAuth.getInstance();
        cekkoneksi();
        etsoal = findViewById(R.id.et_soal);
        etjenissoal = findViewById(R.id.et_jenissoal);
        etstatussoal = findViewById(R.id.et_statussoal);
        database = FirebaseDatabase.getInstance().getReference("soal");

        CardView tambah = findViewById(R.id.tambahsoal);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soall = etsoal.getText().toString();
                String jenissoall = etjenissoal.getText().toString();
                String statussoall = etstatussoal.getText().toString();
                if(soall.equals("")){
                    etsoal.setError("Masukan Soal");
                    etsoal.requestFocus();
                }else if(statussoall.equals("")){
                    etstatussoal.setError("Masukan Status Soal");
                    etstatussoal.requestFocus();
                }else if(jenissoall.equals("")){
                    etjenissoal.setError("Masukan Jenis Soal");
                    etjenissoal.requestFocus();
                }else {
                    loading = ProgressDialog.show(TambahsoalActivity.this,null,"Mohon Tunggu",true,false);
                    //masukan ke db

                    ReqSoal reqSoal = new ReqSoal(soall,jenissoall,statussoall);
                    database.child(jenissoall).child(soall).setValue(reqSoal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loading.dismiss();
                            Toast.makeText(TambahsoalActivity.this,"berhasil menambahkan soal",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),DatasoalActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                }
            }
        });


    }
    //klo pencet kembali
    @Override
    public void onBackPressed() {
        Intent goback= new Intent(getApplicationContext() , DatasoalActivity.class);
        startActivity(goback);
        finish();
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