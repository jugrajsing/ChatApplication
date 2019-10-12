package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.ModelClasses.Social_user_name;
import com.example.hp.chatapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Social_userlistAdapter extends RecyclerView.Adapter<Social_userlistAdapter.SocialHolder> {
    ArrayList<Social_user_name> userlist = new ArrayList<>();
    Context mcontext;

    public Social_userlistAdapter(ArrayList<Social_user_name> userlist, Context mcontext) {
        this.userlist = userlist;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public Social_userlistAdapter.SocialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_userlistitem, parent, false);
        SocialHolder socialHolder = new SocialHolder(view);
        return socialHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Social_userlistAdapter.SocialHolder holder, int position) {
        //  Social_user_name social_user_name=userlist.get(position);
        holder.text_user_list_nickname.setText(userlist.get(position).getText_user_list_nickname());

        // Glide.with(mcontext).load(social_user_name.getImage_user_list_profile()).into(holder.image_user_list_profile);
        //
        //  holder.text_user_list_nickname.setText(social_user_name.getText_user_list_nickname());
        if (userlist.get(position).getImage_user_list_profile().equals("No")) {
            Glide.with(mcontext).load(R.drawable.profile_img).into(holder.image_user_list_profile);

        } else {
            Glide.with(mcontext).load(userlist.get(position).getImage_user_list_profile()).into(holder.image_user_list_profile);

        }
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class SocialHolder extends RecyclerView.ViewHolder {
        CircleImageView image_user_list_profile;
        TextView text_user_list_nickname;

        public SocialHolder(View itemView) {
            super(itemView);
            image_user_list_profile = itemView.findViewById(R.id.user_list_profile);
            text_user_list_nickname = itemView.findViewById(R.id.user_list_nickname);
        }
    }
}
