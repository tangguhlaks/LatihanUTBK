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

import com.example.tesutbk.AdminActivity;
import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.model.JawabanReq;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KeteranganNilai extends AppCompatActivity {
    private String soal;
    private String jenissoal;
    int nilai;
    int benar;
    int salah;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference dbjawaban = FirebaseDatabase.getInstance().getReference("jawaban");
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keterangan_nilai);
        cekkoneksi();
        loading = ProgressDialog.show(KeteranganNilai.this,null,"mohon tunggu",true,false);
        Intent data = getIntent();
        soal = data.getStringExtra("soal");
        jenissoal = data.getStringExtra("jenissoal");

        TextView soaltv = findViewById(R.id.soal);
        TextView jenissoaltv = findViewById(R.id.jenissoal);
        TextView benartv = findViewById(R.id.jawabanbenar);
        TextView salahtv = findViewById(R.id.jawabansalah);
        TextView nilaitv = findViewById(R.id.nilai);

        FirebaseUser user = mAuth.getCurrentUser();
        dbjawaban.child(jenissoal).child(soal).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                for (DataSnapshot item : snapshot.getChildren()){
                    JawabanReq jawabanReq = item.getValue(JawabanReq.class);
                    if(jawabanReq.getKett().equals("benar")){
                        nilai += jawabanReq.getNilai();
                        benar ++;
                    }else if(jawabanReq.getKett().equals("salah")){
                        salah++;
                    }
                }
                soaltv.setText("Nama Soal    : "+soal);
                jenissoaltv.setText("Jenis Soal     : "+jenissoal);
                benartv.setText("Benar             : "+benar);
                salahtv.setText("Salah             : "+salah);
                nilaitv.setText("Nilai               : "+nilai);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public void onBackPressed() {
            Intent i = new Intent(getApplicationContext(),KeteranganSoal.class);
            i.putExtra("soal",soal);
            i.putExtra("jenissoal",jenissoal);
            startActivity(i);
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