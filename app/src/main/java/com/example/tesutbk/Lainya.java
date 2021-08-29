package com.example.tesutbk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Lainya extends AppCompatActivity {
    private String username;
    private String email;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lainya);
        cekkoneksi();
        //get email and username from intent
        Intent datauser = getIntent();
        username = datauser.getStringExtra("username");
        email = datauser.getStringExtra("email");

        TextView usernametv = findViewById(R.id.username);
        TextView emailtv = findViewById(R.id.email);
        usernametv.setText(username);
        emailtv.setText(email);

        //logout
        CardView logout = findViewById(R.id.keluaruser);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"sampai jumpa",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent goLogin= new Intent(getApplicationContext() ,UserActivity.class);
        startActivity(goLogin);
        finish();
    }


    //cek koneksi dan user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getEmail().equals("tangguhlaksana0@gmail.com")){
                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();

        }
    }
    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(getApplicationContext(),NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }

}