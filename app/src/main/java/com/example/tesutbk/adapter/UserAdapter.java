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
import com.example.tesutbk.model.Datauser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    List<Datauser> MList;
    Context context;
    OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public UserAdapter(Context context, List<Datauser> MList){
        this.context = context;
        this.MList = MList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_username.setText("Email           : "+MList.get(position).getEmail());
        holder.tv_email.setText("Username   : "+MList.get(position).getUsername());

        holder.tbl_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onTblHapus(MList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return MList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_email;
        ImageButton tbl_hapus;
        CardView card_view;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_email = itemView.findViewById(R.id.tv_email);
            tbl_hapus = itemView.findViewById(R.id.tbl_hapus);
        }
    }
    public interface OnCallBack{
        void onTblHapus(Datauser datauser);
    }
}
