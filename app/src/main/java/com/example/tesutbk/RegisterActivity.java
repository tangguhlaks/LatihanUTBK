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

import com.example.tesutbk.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etusername;
    private EditText etpassword;
    private EditText etemail;
    private EditText ketemail;
    private String role;
    private DatabaseReference database;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         role ="user";
        etusername = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);
        etemail = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("user");
        cekkoneksi();

        TextView btnlogin = findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin= new Intent(RegisterActivity.this ,LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });


        CardView daftar = findViewById(R.id.daftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= etusername.getText().toString();
                String password = etpassword.getText().toString();
                String email = etemail.getText().toString();

                if (username.equals("")){
                    etusername.setError("Silahkan Isi Username");
                    etusername.requestFocus();
                } else if (password.length() < 6){
                    etpassword.setError("password min 6 karakter");
                    etpassword.requestFocus();
                } else if (email.equals("")){
                    etusername.setError("Silahkan Isi Email");
                    etusername.requestFocus();
                }else if (password.equals("")){
                    etusername.setError("Silahkan Isi Password");
                    etusername.requestFocus();
                }else {
                    loading = ProgressDialog.show(RegisterActivity.this,null,"Mohon Tunggu",true,false);
                    //auth firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //masukan ke db
                                        String key = user.getUid();
                                        Request request = new Request(username,email,role,key);
                                        database.child(key)
                                                .setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading.dismiss();
                                                Intent i = new Intent(RegisterActivity.this,Redirect.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                        // Read from the database


                                    } else {
                                        loading.dismiss();
                                        etusername.setText("");
                                        etemail.setText("");
                                        etpassword.setText("");
                                        Toast.makeText(RegisterActivity.this, "Pendaftaran Gagal , Mungkin Email Sudah Terdaftar",
                                                Toast.LENGTH_LONG).show();

                                    }

                                    // ...
                                }
                            });

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent goLogin= new Intent(RegisterActivity.this ,LoginActivity.class);
        startActivity(goLogin);
        finish();
    }

    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(RegisterActivity.this,NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }

}