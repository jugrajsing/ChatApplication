package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.hardware.camera2.params.Face;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Intefaces.FriendListInterface;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.ModelClasses.SearchedUsersModel;
import com.example.hp.chatapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    ArrayList<FriendListModel> friendListModelArrayList;
    private Context context;

    FriendListInterface friendListInterface;

    public FriendListAdapter(ArrayList<FriendListModel> friendListModelArrayList, Context context,FriendListInterface friendListInterface) {
        this.friendListModelArrayList = friendListModelArrayList;
        this.context = context;
        this.friendListInterface=friendListInterface;
    }

    @Override public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list , parent, false);
        return new FriendListAdapter.ViewHolder(v,friendListInterface);
    }

    @Override public void onBindViewHolder(final FriendListAdapter.ViewHolder holder, final int position) {
        final FriendListModel item = friendListModelArrayList.get(position);
        String user_name=item.getName();
        String sex=item.getGender();
        String secret_id=item.getSecret_id();
        String user_image=item.getImage();

        holder.searched_user_name.setText(user_name);
        holder.searched_user_gender.setText(sex);
        holder.searched_user_secretKey.setText(secret_id);
        //  holder.searched_user_mobile.setText(mobile_no);
        Glide.with(context).load(user_image).into(holder.searched_user_image);

    }

    @Override public int getItemCount() {
        return friendListModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView searched_user_image;
        public TextView searched_user_secretKey,searched_user_name,searched_user_gender;
        public Button send_friend_request,cancle_friend_request;
        FriendListInterface friendListInterface;


        public ViewHolder(final View itemView, final FriendListInterface friendListInterface) {
            super(itemView);

            this.friendListInterface=friendListInterface;

            searched_user_secretKey= (TextView) itemView.findViewById(R.id.searched_user_secretKey);
            searched_user_gender= (TextView) itemView.findViewById(R.id.searched_user_gender);

            searched_user_image = (CircleImageView) itemView.findViewById(R.id.searched_user_image);
            searched_user_name = (TextView) itemView.findViewById(R.id.searched_user_name);
            send_friend_request=(Button) itemView.findViewById(R.id.send_friend_request);
            cancle_friend_request=(Button) itemView.findViewById(R.id.cancle_friend_request);
            send_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendListInterface.messageFriend(view ,getAdapterPosition());
                }
            });

            cancle_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendListInterface.unfriedFriends(view , getAdapterPosition());
                }
            });
            // searched_user_mobile = (TextView) itemView.findViewById(R.id.searched_user_mobile);

        }
    }
}
