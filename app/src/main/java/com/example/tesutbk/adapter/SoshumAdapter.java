package com.example.tesutbk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSoal;

import java.util.List;

public class SoshumAdapter extends RecyclerView.Adapter<SoshumAdapter.MyViewHolder> {
    List<ReqSoal> Mlist;
    Context context;
    OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public SoshumAdapter(List<ReqSoal> mlist, Context context) {
        Mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_soshum,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_soal.setText("Nama Soal   : "+Mlist.get(position).getSoal());
        holder.tv_jenissoal.setText("Tipe Soal      : "+Mlist.get(position).getJenissoal());
        holder.tv_statussoal.setText("Status Soal  : "+Mlist.get(position).getStatus());

        holder.tbl_hapussoshum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onHapussoshum(Mlist.get(position));
            }
        });
        holder.tbl_editsoshum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onEditsoshum(Mlist.get(position));
            }
        });
        holder.tbl_subsoshum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onSubSoshum(Mlist.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return Mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_soal;
        TextView tv_jenissoal;
        TextView tv_statussoal;
        ImageButton tbl_hapussoshum;
        ImageButton tbl_editsoshum;
        ImageButton tbl_subsoshum;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soal = itemView.findViewById(R.id.tv_soalsoshum);
            tv_jenissoal = itemView.findViewById(R.id.tv_jenissoalsoshum);
            tv_statussoal = itemView.findViewById(R.id.tv_statussoalsoshum);
            tbl_hapussoshum = itemView.findViewById(R.id.tbl_hapussoshum);
            tbl_editsoshum = itemView.findViewById(R.id.tbl_editsoshum);
            tbl_subsoshum = itemView.findViewById(R.id.tbl_subsoshum);
        }
    }
    public interface OnCallBack{
        void onHapussoshum(ReqSoal reqSoal);
        void onEditsoshum(ReqSoal reqSoal);
        void onSubSoshum(ReqSoal reqSoal);
    }
}
