package com.example.tesutbk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NointernetActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        //login&register
        CardView fresh = findViewById(R.id.refresh);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekkoneksi();
            }
        });

    }

    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null!=activenetwork){
            Toast.makeText(NointernetActivity.this, " Terhubung kembali dengan jaringan",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(NointernetActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(NointernetActivity.this, "Tidak dapat terhubung dengan jaringan",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
