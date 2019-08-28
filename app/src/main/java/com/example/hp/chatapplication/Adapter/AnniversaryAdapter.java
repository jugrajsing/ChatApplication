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
import com.example.hp.chatapplication.ModelClasses.AnniversaryModel;
import com.example.hp.chatapplication.ModelClasses.BirthdayModel;
import com.example.hp.chatapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnniversaryAdapter extends RecyclerView.Adapter<AnniversaryAdapter.AnniverSaryHolder> {
    Context mcontext;
    List<AnniversaryModel> anniversaryModelList;

    public AnniversaryAdapter(Context mcontext, List<AnniversaryModel> anniversaryModelList) {
        this.mcontext = mcontext;
        this.anniversaryModelList = anniversaryModelList;
    }

    @NonNull
    @Override
    public AnniversaryAdapter.AnniverSaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.anniversary_items , parent, false);
        return new AnniversaryAdapter.AnniverSaryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnniversaryAdapter.AnniverSaryHolder holder, int position) {
        AnniversaryModel anniversaryModel=anniversaryModelList.get(position);
        holder.tv_anniversary.setText(anniversaryModel.getAnniversary());
        holder.tv_name.setText(anniversaryModel.getName());
        Glide.with(mcontext).load(anniversaryModel.getImage()).into( holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return anniversaryModelList.size();
    }

    public class AnniverSaryHolder extends RecyclerView.ViewHolder {
        TextView tv_anniversary,tv_name;

        CircleImageView iv_image;

        public AnniverSaryHolder(View itemView) {
            super(itemView);
            tv_anniversary=itemView.findViewById(R.id.tv_anniversary);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_image=itemView.findViewById(R.id.iv_image);

        }
    }
}

