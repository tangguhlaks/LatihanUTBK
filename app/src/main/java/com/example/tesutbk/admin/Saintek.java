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
import com.example.tesutbk.adapter.SaintekAdapter;
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

public class Saintek extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SaintekAdapter saintekAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("soal");
    DatabaseReference databasesub = FirebaseDatabase.getInstance().getReference("subsoal");
    DatabaseReference databasejawaban = FirebaseDatabase.getInstance().getReference("jawaban");
    ArrayList<ReqSoal> listSaintek = new ArrayList<>();
    RecyclerView rcviewSaintek;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saintek);
        mAuth = FirebaseAuth.getInstance();
        cekkoneksi();
        rcviewSaintek = findViewById(R.id.rcviewSaintek);
        rcviewSaintek.setLayoutManager(new LinearLayoutManager(this));
        loading = ProgressDialog.show(Saintek.this,null,"mohon tunggu",true,false);
        showData();

    }

    private void showData() {
        database.child("SAINTEK").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSaintek.clear();
                loading.dismiss();
                for (DataSnapshot item:snapshot.getChildren()){
                    ReqSoal reqSoal = item.getValue(ReqSoal.class);
                    listSaintek.add(reqSoal);
                }
                saintekAdapter = new SaintekAdapter(Saintek.this,listSaintek);
                rcviewSaintek.setAdapter(saintekAdapter);
                setClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG","failed to read value",error.toException());
            }
        });
    }

    private void setClick() {
        saintekAdapter.setOnCallBack(new SaintekAdapter.OnCallBack() {
            @Override
            public void onHapussaintek(ReqSoal reqSoal) {
                new AlertDialog.Builder(Saintek.this)
                        .setIcon(R.drawable.logoutama)
                        .setTitle(R.string.app_name)
                        .setMessage("Hapus Soal "+reqSoal.getSoal())
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusSaintek(reqSoal);
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
            public void onEditSaintek(ReqSoal reqSoal) {
                new AlertDialog.Builder(Saintek.this)
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
            public void onSubSaintek(ReqSoal reqSoal) {
                Intent i = new Intent(getApplicationContext(),Subsoal.class);
                i.putExtra("soal",reqSoal.getSoal());
                i.putExtra("context","Saintek");
                i.putExtra("jenissoal",reqSoal.getJenissoal());
                startActivity(i);
                finish();
            }
        });
    }

    private void editstatus(ReqSoal reqSoal) {
        ReqSoal edit;
        if (reqSoal.getStatus().equals("aktif")){
            edit = new ReqSoal(reqSoal.getSoal(),reqSoal.getJenissoal(),"tidak");
        }else{
            edit = new ReqSoal(reqSoal.getSoal(),reqSoal.getJenissoal(),"aktif");
        }
        database.child(reqSoal.getJenissoal()).child(reqSoal.getSoal()).setValue(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Status Berhasil Diubah",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusSaintek(ReqSoal reqSoal) {
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
            Intent i = new Intent(Saintek.this, NointernetActivity.class);
            startActivity(i);
            finish();
        }
    }

}