package com.example.tesutbk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSoal;

import java.util.ArrayList;

public class SoalAdapter extends RecyclerView.Adapter<SoalAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReqSoal> mList;
    OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public SoalAdapter(Context context, ArrayList<ReqSoal> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_soaluser,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.soal.setText(mList.get(position).getSoal());

        holder.cardsoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onCardClick(mList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView soal;
        CardView cardsoal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            soal = itemView.findViewById(R.id.soal);
            cardsoal = itemView.findViewById(R.id.card_soal);
        }
    }
    public interface OnCallBack{
        void onCardClick(ReqSoal reqSoal);
    }
}
