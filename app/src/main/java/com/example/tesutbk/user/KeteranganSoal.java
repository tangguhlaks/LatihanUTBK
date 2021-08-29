package com.example.tesutbk.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesutbk.AdminActivity;
import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.model.JawabanReq;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KeteranganSoal extends AppCompatActivity {
    private String soal;
    private String jenissoal;
    private String userid;
    private int nilai;
    int n = 0;
    String kett;
    String nilaiuser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbjawaban = FirebaseDatabase.getInstance().getReference("jawaban");
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keterangan_soal);
        cekkoneksi();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        loading = ProgressDialog.show(KeteranganSoal.this,null,"mohon tunggu",true,false);

        Intent data = getIntent();
        soal = data.getStringExtra("soal");
        jenissoal = data.getStringExtra("jenissoal");

        TextView soaltv = findViewById(R.id.soal);
        TextView jenissoaltv = findViewById(R.id.jenissoal);
        TextView keterangan = findViewById(R.id.keterangan);
        TextView nilaitv = findViewById(R.id.nilai);

        dbjawaban.child(jenissoal).child(soal).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                //for user interface
                for (DataSnapshot item : snapshot.getChildren()){
                    JawabanReq jawabanReq = item.getValue(JawabanReq.class);
                    n++;
                    if (jawabanReq.getKett().equals("benar")){
                        nilai += jawabanReq.getNilai();
                    }
                }
                if (n == 5){
                    kett = "Sudah";
                    nilaiuser=""+nilai;

                }else {
                    kett="Belum";
                    nilaiuser="-";
                }
                soaltv.setText("Nama Soal    : "+soal);
                jenissoaltv.setText("Jenis Soal     : "+jenissoal);
                keterangan.setText("Keterangan   : "+kett);
                nilaitv.setText("Nilai               : "+nilaiuser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ImageButton mulai = findViewById(R.id.mulai);
        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kett.equals("Belum")){
                    Intent i = new Intent(getApplicationContext(),Latihan.class);
                    i.putExtra("soal",soal);
                    i.putExtra("jenissoal",jenissoal);
                    i.putExtra("nomor",1);
                    startActivity(i);
                    finish();
                }else{
                    new AlertDialog.Builder(KeteranganSoal.this)
                            .setIcon(R.drawable.logoutama).setTitle(R.string.app_name).setMessage("Kamu sudah mengerjakan soal ini, nilai pengerjaan yang sudah ada akan terhapus")
                            .setPositiveButton("Mulai", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   dbjawaban.child(jenissoal).child(soal).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           dialog.cancel();
                                           Intent i = new Intent(getApplicationContext(),Latihan.class);
                                           i.putExtra("soal",soal);
                                           i.putExtra("jenissoal",jenissoal);
                                           i.putExtra("nomor",1);
                                           startActivity(i);
                                           finish();
                                       }
                                   });
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
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent goLogin= new Intent(getApplicationContext() , SoalUser.class);
        goLogin.putExtra("jenissoal",jenissoal);
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