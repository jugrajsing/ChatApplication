package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.ModelClasses.BirthdayModel;
import com.example.hp.chatapplication.R;

import java.util.List;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.BirthdayHolder> {
    Context mcontext;
    List<BirthdayModel> birthdaylist;

    public BirthdayAdapter(Context mcontext, List<BirthdayModel> birthdaylist) {
        this.mcontext = mcontext;
        this.birthdaylist = birthdaylist;
    }

    @NonNull
    @Override
    public BirthdayAdapter.BirthdayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.birthdayitem , parent, false);
        return new BirthdayHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdayAdapter.BirthdayHolder holder, int position) {
        BirthdayModel birthdayModel=birthdaylist.get(position);
        holder.tv_date_of_birth.setText(birthdayModel.getDob());
        holder.tv_name.setText(birthdayModel.getName());
        Glide.with(mcontext).load(birthdayModel.getImg()).into( holder.user_img);
    }

    @Override
    public int getItemCount() {
        return birthdaylist.size();
    }

    public class BirthdayHolder extends RecyclerView.ViewHolder {
        TextView tv_date_of_birth,tv_name;
        ImageView user_img;
        public BirthdayHolder(View itemView) {
            super(itemView);

            tv_date_of_birth=itemView.findViewById(R.id.tv_date_of_birth);
            tv_name=itemView.findViewById(R.id.tv_name);
            user_img=itemView.findViewById(R.id.profile_bimg);

        }
    }
}
