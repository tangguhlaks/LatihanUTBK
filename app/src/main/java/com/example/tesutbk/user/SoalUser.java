package com.example.tesutbk.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.AdminActivity;
import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.UserActivity;
import com.example.tesutbk.adapter.SoalAdapter;
import com.example.tesutbk.model.ReqSoal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SoalUser extends AppCompatActivity {
    private String jenissoal;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference dbSoal = FirebaseDatabase.getInstance().getReference("soal");
    ArrayList<ReqSoal> soalList = new ArrayList<>();
    ProgressDialog loading;
    SoalAdapter soalAdapter;
    RecyclerView rcviewsoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_user);
        cekkoneksi();

        Intent data = getIntent();
        jenissoal = data.getStringExtra("jenissoal");

        TextView title = findViewById(R.id.title);
        title.setText("Soal Soal "+jenissoal);

        rcviewsoal = findViewById(R.id.rcviewsoal);
        rcviewsoal.setLayoutManager(new LinearLayoutManager(this));
        loading = ProgressDialog.show(SoalUser.this,null,"mohon tunggu",true,false);
        showData();
    }

    private void showData() {
        dbSoal.child(jenissoal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                soalList.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    ReqSoal reqSoal = item.getValue(ReqSoal.class);
                    if(reqSoal.getStatus().equals("aktif")){
                        soalList.add(reqSoal);
                    }
                }
                soalAdapter = new SoalAdapter(SoalUser.this,soalList);
                rcviewsoal.setAdapter(soalAdapter);
                setClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setClick() {
        soalAdapter.setOnCallBack(new SoalAdapter.OnCallBack() {
            @Override
            public void onCardClick(ReqSoal reqSoal) {
                Intent i = new Intent(getApplicationContext(),KeteranganSoal.class);
                i.putExtra("soal",reqSoal.getSoal());
                i.putExtra("jenissoal",reqSoal.getJenissoal());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goLogin= new Intent(getApplicationContext() , UserActivity.class);
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
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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