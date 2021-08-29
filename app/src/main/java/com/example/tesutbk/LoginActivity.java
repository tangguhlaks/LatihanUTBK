package com.example.tesutbk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etemail;
    private EditText etpassword;
    final private String TAG="user";
    private ProgressDialog loading;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        etemail = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        database = FirebaseDatabase.getInstance();
        cekkoneksi();



        TextView btnregister = findViewById(R.id.btn_register);
        btnregister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent goRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goRegister);
                finish();

            }
        });
        CardView masuk = findViewById(R.id.masuk);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                if (email.equals("")){
                    etemail.setError("Silahkan Isi Email");
                    etemail.requestFocus();
                }else if (password.equals("")){
                    etpassword.setError("Silahkan Isi Password");
                    etpassword.requestFocus();
                }else{
                    loading = ProgressDialog.show(LoginActivity.this,null,"Mohon Tunggu",true,false);
                    mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loading.dismiss();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Selamat Datang",
                                                Toast.LENGTH_SHORT).show();
                                        if (user.getEmail().equals("tangguhlaksana0@gmail.com")){
                                            Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            Intent i = new Intent(LoginActivity.this,UserActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    } else {
                                        loading.dismiss();
                                        Toast.makeText(LoginActivity.this, "Email atau Password salah dan cek koneksi Internet Kamu",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }

            }
        });
    }


//koneksi dan user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getEmail().equals("tangguhlaksana0@gmail.com")){
                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(LoginActivity.this,UserActivity.class);
                startActivity(i);
                finish();
            }
        }
    }



    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(LoginActivity.this,NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }
}