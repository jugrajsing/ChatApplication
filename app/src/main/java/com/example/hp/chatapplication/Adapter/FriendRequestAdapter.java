package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    ArrayList<FriendListModel> friendListModelArrayList;
    private Context context;
    private RecyclerViewAddFriendClickListener mListener;

    public FriendRequestAdapter(ArrayList<FriendListModel> friendListModelArrayList, Context context,RecyclerViewAddFriendClickListener mListener) {
        this.friendListModelArrayList = friendListModelArrayList;
        this.mListener = mListener;
        this.context = context;
    }

    @Override public FriendRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_items , parent, false);

        return new FriendRequestAdapter.ViewHolder(v,mListener);
    }

    @Override public void onBindViewHolder(final FriendRequestAdapter.ViewHolder holder, final int position) {
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
        public Button send_friend_request,btn_unfriend;
        private RecyclerViewAddFriendClickListener mListener;


        public ViewHolder(final View itemView, RecyclerViewAddFriendClickListener recyclerViewAddFriendClickListener) {
            super(itemView);
            this.mListener =recyclerViewAddFriendClickListener;
            searched_user_secretKey= (TextView) itemView.findViewById(R.id.searched_user_secretKey);
            searched_user_gender= (TextView) itemView.findViewById(R.id.searched_user_gender);

            searched_user_image = (CircleImageView) itemView.findViewById(R.id.searched_user_image);
            searched_user_name = (TextView) itemView.findViewById(R.id.searched_user_name);
            send_friend_request=(Button) itemView.findViewById(R.id.send_friend_request);
            btn_unfriend=(Button) itemView.findViewById(R.id.btn_unfriend);
            // searched_user_mobile = (TextView) itemView.findViewById(R.id.searched_user_mobile);
            btn_unfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCancleFriendRequest(v,getAdapterPosition());
                }
            });

            send_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddFriend(v,getAdapterPosition());
                }
            });

        }
    }


}

