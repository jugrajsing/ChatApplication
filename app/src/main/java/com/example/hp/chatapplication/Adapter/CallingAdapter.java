package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.chatapplication.ModelClasses.Callhistory_Model;
import com.example.hp.chatapplication.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallingAdapter extends RecyclerView.Adapter<CallingAdapter.CallHolder> {
    Context callcontext;
    List<Callhistory_Model> calllist = new ArrayList<>();

    public CallingAdapter(Context callcontext, List<Callhistory_Model> calllist) {
        this.callcontext = callcontext;
        this.calllist = calllist;
    }


    @NonNull
    @Override
    public CallingAdapter.CallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item, parent, false);
        CallHolder socialHolder = new CallHolder(view);
        return socialHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CallingAdapter.CallHolder holder, int position) {
        holder.caller_name.setText(calllist.get(position).getCallername());
        holder.call_time.setText(calllist.get(position).getCalling_time());
        //  holder.caller_img.setImageResource(calllist.get(position).getCaller_profile());
        holder.call_time.setText(calllist.get(position).getCalling_status_text());
        // holder.caller_img.setImageDrawable(calllist.get(position).getCaller_profile());
    }

    @Override
    public int getItemCount() {
        return calllist.size();
    }

    public class CallHolder extends RecyclerView.ViewHolder {
        CircleImageView caller_img;
        ImageView log_call, call_btn;
        TextView caller_name, call_time;

        public CallHolder(View itemView) {
            super(itemView);
            caller_img = itemView.findViewById(R.id.caller_img);
            log_call = itemView.findViewById(R.id.log_call);
            call_btn = itemView.findViewById(R.id.call_btn);
            caller_name = itemView.findViewById(R.id.caller_name);
            call_time = itemView.findViewById(R.id.call_time);


        }
    }
}
