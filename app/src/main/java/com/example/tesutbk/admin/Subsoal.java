package com.example.tesutbk.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.adapter.SubSoalAdapter;
import com.example.tesutbk.model.ReqSubSoal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Subsoal extends AppCompatActivity {
    TextView soalsub;
    String soal;
    String jenissoal;
    String contextback;
    FirebaseAuth mAuth;

    DatabaseReference dbSub = FirebaseDatabase.getInstance().getReference("subsoal");
    SubSoalAdapter subSoalAdapter;
    RecyclerView rcviewsub ;
    ArrayList<ReqSubSoal> subList = new ArrayList<>();
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsoal);
        mAuth = FirebaseAuth.getInstance();
        cekkoneksi();



        Intent parameter = getIntent();
        soal = parameter.getStringExtra("soal");
        contextback = parameter.getStringExtra("context");
        jenissoal = parameter.getStringExtra("jenissoal");

        soalsub = findViewById(R.id.soalsub);
        soalsub.setText("SubSoal "+soal);

        Button tambahsubsoal = findViewById(R.id.tambahsubsoal);
        tambahsubsoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TambahSubsoal.class);
                i.putExtra("soal",soal);
                i.putExtra("jenissoal",jenissoal);
                startActivity(i);
                finish();
            }
        });

        rcviewsub = findViewById(R.id.rcviewsub);
        rcviewsub.setLayoutManager(new LinearLayoutManager(this));
        loading = ProgressDialog.show(Subsoal.this,null,"mohon tunggu",true,false);
        showData();

    }

    private void showData() {
        dbSub.child(jenissoal).child(soal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subList.clear();
                loading.dismiss();
                for(DataSnapshot item : snapshot.getChildren()){
                    ReqSubSoal reqSubSoal = item.getValue(ReqSubSoal.class);
                    subList.add(reqSubSoal);
                }
                subSoalAdapter = new SubSoalAdapter(subList,Subsoal.this);
                rcviewsub.setAdapter(subSoalAdapter);
                setclick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setclick() {
        subSoalAdapter.setOnCallBack(new SubSoalAdapter.OnCallBack() {
            @Override
            public void onHapusSub(ReqSubSoal reqSubSoal) {
                new AlertDialog.Builder(Subsoal.this)
                        .setIcon(R.drawable.logoutama)
                        .setTitle(R.string.app_name)
                        .setMessage("Hapus Soal No "+reqSubSoal.getNo())
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapussub(reqSubSoal);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

    }

    private void hapussub(ReqSubSoal reqSubSoal) {
        dbSub.child(jenissoal).child(soal).child(reqSubSoal.getNo()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
             Toast.makeText(getApplicationContext(),"berhasil dihapus",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //klo pencet kembali
    @Override
    public void onBackPressed() {
        if (contextback.equals("Saintek")){
            Intent goback= new Intent(getApplicationContext() , Saintek.class);
            startActivity(goback);
            finish();
        }else if (contextback.equals("Soshum")){
            Intent goback= new Intent(getApplicationContext() , Soshum.class);
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