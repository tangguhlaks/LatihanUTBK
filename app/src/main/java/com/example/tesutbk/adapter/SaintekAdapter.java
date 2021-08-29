package com.example.tesutbk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSoal;

import java.util.List;

public class SaintekAdapter extends RecyclerView.Adapter<SaintekAdapter.MyViewHolder> {
    List<ReqSoal> Mlist;
    Context context;
    OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public SaintekAdapter(Context context, List<ReqSoal> Mlist) {
        this.context = context;
        this.Mlist = Mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_saintek,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_soal.setText("Nama Soal   : "+Mlist.get(position).getSoal());
        holder.tv_jenissoal.setText("Tipe Soal      : "+Mlist.get(position).getJenissoal());
        holder.tv_statussoal.setText("Status Soal  : "+Mlist.get(position).getStatus());

        holder.tbl_hapussaintek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onHapussaintek(Mlist.get(position));
            }
        });
        holder.tbl_editsaintek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onEditSaintek(Mlist.get(position));
            }
        });
        holder.tbl_subsaintek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onSubSaintek(Mlist.get(position));
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
        ImageButton tbl_hapussaintek;
        ImageButton tbl_editsaintek;
        ImageButton tbl_subsaintek;
        CardView card_saintek;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soal = itemView.findViewById(R.id.tv_soal);
            tv_jenissoal = itemView.findViewById(R.id.tv_jenissoal);
            tv_statussoal = itemView.findViewById(R.id.tv_statussoal);
            tbl_hapussaintek = itemView.findViewById(R.id.tbl_hapussaintek);
            tbl_editsaintek = itemView.findViewById(R.id.tbl_editsaintek);
            tbl_subsaintek = itemView.findViewById(R.id.tbl_subsaintek);
        }
    }
    public interface OnCallBack{
        void onHapussaintek(ReqSoal reqSoal);
        void onEditSaintek(ReqSoal reqSoal);
        void onSubSaintek(ReqSoal reqSoal);
    }
}
