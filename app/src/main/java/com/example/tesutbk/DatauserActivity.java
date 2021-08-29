package com.example.tesutbk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.adapter.UserAdapter;
import com.example.tesutbk.model.Datauser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatauserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    UserAdapter userAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
    ArrayList<Datauser> listUser = new ArrayList<>();
    RecyclerView rcview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datauser);
        mAuth = FirebaseAuth.getInstance();
        cekkoneksi();
        rcview = findViewById(R.id.rcview);
        rcview.setLayoutManager(new LinearLayoutManager(this));
        showData();


   
    }


    private void showData(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    Datauser usr = item.getValue(Datauser.class);
                    listUser.add(usr);
                }
                userAdapter =new UserAdapter(DatauserActivity.this,listUser);
                rcview.setAdapter(userAdapter);
                setClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG","failed to read value",error.toException());
            }
        });
    }

    private void setClick() {
        userAdapter.setOnCallBack(new UserAdapter.OnCallBack() {
            @Override
            public void onTblHapus(Datauser datauser) {
                hapusData(datauser);
            }
        });
    }

    private void hapusData(Datauser datauser) {
        database.child(datauser.getKey()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(),"Data Berhasil Dihapus",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //klo pencet kembali
    @Override
    public void onBackPressed() {
        Intent goback= new Intent(DatauserActivity.this ,AdminActivity.class);
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
                Intent i = new Intent(DatauserActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(DatauserActivity.this, "Login Dulu Dong",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DatauserActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
    }
    public void cekkoneksi(){
        ConnectivityManager Manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = Manager.getActiveNetworkInfo();
        if (null==activenetwork){
            Intent i = new Intent(DatauserActivity.this,NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }

}