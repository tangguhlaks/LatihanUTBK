package com.example.tesutbk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesutbk.R;
import com.example.tesutbk.model.ReqSoal;

import java.util.List;

public class AdapterTes extends RecyclerView.Adapter<AdapterTes.MyViewHolder> {
    Context context;
    List<ReqSoal> mList;

    public AdapterTes(Context context, List<ReqSoal> mList) {
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
        //holder disini

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //object tampilan disini
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
