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
import com.example.tesutbk.model.ReqSubSoal;

import java.util.ArrayList;

public class SubSoalAdapter extends RecyclerView.Adapter<SubSoalAdapter.MyViewHolder> {
    ArrayList<ReqSubSoal> mList;
    Context context;
    OnCallBack onCallBack;

    public SubSoalAdapter(ArrayList<ReqSubSoal> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_subsoal,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.no.setText("No   : "+mList.get(position).getNo());
        holder.pertanyaan.setText("Pertanyaan   : "+mList.get(position).getPertanyaan());
        holder.a.setText("A : "+mList.get(position).getA());
        holder.b.setText("B : "+mList.get(position).getB());
        holder.c.setText("C : "+mList.get(position).getC());
        holder.d.setText("D : "+mList.get(position).getD());
        holder.benar.setText("Pilihan Benar : "+mList.get(position).getBenar());

        holder.tbl_hapussub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onHapusSub(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView no;
        TextView pertanyaan;
        TextView a;
        TextView b;
        TextView c;
        TextView d;
        TextView benar;
        ImageButton tbl_hapussub;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.sub_no);
            pertanyaan = itemView.findViewById(R.id.sub_pertanyaan);
            a = itemView.findViewById(R.id.sub_a);
            b = itemView.findViewById(R.id.sub_b);
            c = itemView.findViewById(R.id.sub_c);
            d = itemView.findViewById(R.id.sub_d);
            benar = itemView.findViewById(R.id.sub_benar);
            tbl_hapussub = itemView.findViewById(R.id.tbl_hapussub);
        }
    }
    public interface OnCallBack{
        void onHapusSub(ReqSubSoal reqSubSoal);
    }
}
