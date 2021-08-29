package com.example.tesutbk.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.example.tesutbk.adapter.SoshumAdapter;
import com.example.tesutbk.model.ReqSoal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Soshum extends AppCompatActivity {
    ArrayList<ReqSoal> soshumList = new ArrayList<>();
    SoshumAdapter soshumAdapter;
    RecyclerView rcviewSoshum;
    private FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("soal");
    DatabaseReference databasesub = FirebaseDatabase.getInstance().getReference("subsoal");
    DatabaseReference databasejawaban = FirebaseDatabase.getInstance().getReference("jawaban");
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soshum);
        mAuth = FirebaseAuth.getInstance();
        cekkoneksi();
        rcviewSoshum = findViewById(R.id.rcviewSoshum);
        rcviewSoshum.setLayoutManager(new LinearLayoutManager(this));
        loading= ProgressDialog.show(Soshum.this,null,"mohon tunggu",true,false);
        showData();


    }

    private void showData() {
        database.child("SOSHUM").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                soshumList.clear();
                loading.dismiss();
                for (DataSnapshot item : snapshot.getChildren()){
                    ReqSoal reqSoal = item.getValue(ReqSoal.class);
                    soshumList.add(reqSoal);
                }
                soshumAdapter = new SoshumAdapter(soshumList,Soshum.this);
                rcviewSoshum.setAdapter(soshumAdapter);
                setClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag","error",error.toException());
            }
        });
    }

    private void setClick() {
        soshumAdapter.setOnCallBack(new SoshumAdapter.OnCallBack() {
            @Override
            public void onHapussoshum(ReqSoal reqSoal) {
                new AlertDialog.Builder(Soshum.this)
                        .setIcon(R.drawable.logoutama)
                        .setTitle(R.string.app_name)
                        .setMessage("Hapus Soal "+reqSoal.getSoal())
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapussoshum(reqSoal);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }

            @Override
            public void onEditsoshum(ReqSoal reqSoal) {
                new AlertDialog.Builder(Soshum.this)
                        .setIcon(R.drawable.logoutama).setTitle(R.string.app_name).setMessage("Ubah Status Soal "+reqSoal.getSoal()+" ?")
                        .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editstatus(reqSoal);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }

            @Override
            public void onSubSoshum(ReqSoal reqSoal) {
                Intent i = new Intent(getApplicationContext(),Subsoal.class);
                i.putExtra("soal",reqSoal.getSoal());
                i.putExtra("context","Soshum");
                i.putExtra("jenissoal",reqSoal.getJenissoal());
                startActivity(i);
                finish();
            }
        });
    }

    private void editstatus(ReqSoal reqSoal) {
        ReqSoal edit;
        if(reqSoal.getStatus().equals("aktif")){
            edit = new ReqSoal(reqSoal.getSoal(),reqSoal.getJenissoal(),"tidak");
        }else {
            edit = new ReqSoal(reqSoal.getSoal(),reqSoal.getJenissoal(),"aktif");
        }
        database.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).setValue(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Status Data Berhasil Diubah",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapussoshum(ReqSoal reqSoal) {
        database.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                databasesub.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).removeValue();
                databasejawaban.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).removeValue();
                Toast.makeText(getApplicationContext(),"Data Berhasil Dihapus",Toast.LENGTH_SHORT).show();
            }
        });
        databasesub.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(),"Data Berhasil Dihapus",Toast.LENGTH_SHORT).show();
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