package com.example.tesutbk.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesutbk.AdminActivity;
import com.example.tesutbk.LoginActivity;
import com.example.tesutbk.NointernetActivity;
import com.example.tesutbk.R;
import com.example.tesutbk.model.JawabanReq;
import com.example.tesutbk.model.ReqSubSoal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Latihan extends AppCompatActivity {
    int nomor;
    private String soal;
    private String jenissoal;
    private String jawabanbenar;
    String nomors;
    int nilai=0;


    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    DatabaseReference dbsubsoal = FirebaseDatabase.getInstance().getReference("subsoal");
    DatabaseReference dbjawaban = FirebaseDatabase.getInstance().getReference("jawaban");
    ProgressDialog loading;

    RadioGroup radioGroup;
    RadioButton radioButton;


    TextView pilihanmu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan);
        cekkoneksi();

        Intent data = getIntent();
        soal = data.getStringExtra("soal");
        jenissoal = data.getStringExtra("jenissoal");
        nomor = data.getIntExtra("nomor",1);

        TextView pertanyaan = findViewById(R.id.pertanyaan);
        TextView nomorsoalll = findViewById(R.id.no);
        TextView soaltv = findViewById(R.id.soal);
        TextView a = findViewById(R.id.a);
        TextView b = findViewById(R.id.b);
        TextView c = findViewById(R.id.c);
        TextView d = findViewById(R.id.d);
        pilihanmu = findViewById(R.id.pilihanmu);
        radioGroup = findViewById(R.id.radiogrup);
        RadioButton radioA = findViewById(R.id.radioA);
        RadioButton radioB = findViewById(R.id.radioB);
        RadioButton radioC = findViewById(R.id.radioC);
        RadioButton radioD = findViewById(R.id.radioD);

        soaltv.setText(soal);
        nomors =""+nomor;
        loading = ProgressDialog.show(Latihan.this,null,"mohon tunggu",true,false);
        dbsubsoal.child(jenissoal).child(soal).child(nomors).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                ReqSubSoal reqsoal = snapshot.getValue(ReqSubSoal.class);
                pertanyaan.setText(reqsoal.getPertanyaan());
                nomorsoalll.setText("No  : "+reqsoal.getNo());
                a.setText("A. "+reqsoal.getA());
                b.setText("B. "+reqsoal.getB());
                c.setText("C. "+reqsoal.getC());
                d.setText("D. "+reqsoal.getD());
                jawabanbenar = reqsoal.getBenar();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        FirebaseUser user = mAuth.getCurrentUser();
        dbjawaban.child(jenissoal).child(soal).child(user.getUid()).child(nomors).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JawabanReq jawabanReq = snapshot.getValue(JawabanReq.class);
                if(jawabanReq == null){
                    pilihanmu.setText("-Kamu belum memilih jawaban-");
                    radioA.setChecked(true);
                }else {
                    pilihanmu.setText(jawabanReq.getJawaban());
                    if (jawabanReq.getJawaban().equals("A")) {
                        radioA.setChecked(true);
                    } else if (jawabanReq.getJawaban().equals("B")) {
                        radioB.setChecked(true);
                    }
                    if (jawabanReq.getJawaban().equals("C")) {
                        radioC.setChecked(true);
                    } else if (jawabanReq.getJawaban().equals("D")) {
                        radioD.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomor++;
                if (nomor > 5){
                    Intent i = new Intent(getApplicationContext(),KeteranganNilai.class);
                    i.putExtra("soal",soal);
                    i.putExtra("jenissoal",jenissoal);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(getApplicationContext(),Latihan.class);
                    i.putExtra("soal",soal);
                    i.putExtra("jenissoal",jenissoal);
                    i.putExtra("nomor",nomor);
                    startActivity(i);
                    finish();
                }
            }
        });


        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomor--;
                if (nomor < 1){
                    Intent i = new Intent(getApplicationContext(),KeteranganSoal.class);
                    i.putExtra("soal",soal);
                    i.putExtra("jenissoal",jenissoal);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(getApplicationContext(),Latihan.class);
                    i.putExtra("soal",soal);
                    i.putExtra("jenissoal",jenissoal);
                    i.putExtra("nomor",nomor);
                    startActivity(i);
                    finish();
                }
            }
        });

    }
    //cekradiopilihan
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String jawaban = ""+radioButton.getText();
        FirebaseUser user = mAuth.getCurrentUser();
        String keteranganjawaban;
        if(jawaban.equals(jawabanbenar)){
            nilai = 2;
            keteranganjawaban = "benar";
        }else{
            keteranganjawaban = "salah";
        }
        JawabanReq jawabanReq = new JawabanReq(jawaban,nilai,keteranganjawaban);
        dbjawaban.child(jenissoal).child(soal).child(user.getUid()).child(nomors).setValue(jawabanReq).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pilihanmu.setText(jawaban);
            }
        });

    }


    @Override
    public void onBackPressed() {
        nomor--;
        if (nomor < 1){
            Intent i = new Intent(getApplicationContext(),KeteranganSoal.class);
            i.putExtra("soal",soal);
            i.putExtra("jenissoal",jenissoal);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(getApplicationContext(),Latihan.class);
            i.putExtra("soal",soal);
            i.putExtra("jenissoal",jenissoal);
            i.putExtra("nomor",nomor);
            startActivity(i);
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