package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.ModelClasses.SearchedUsersModel;
import com.example.hp.chatapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    //private List<SearchedUsersModel> items;
    ArrayList<SearchedUsersModel> searchedUsersModelArrayList;
    private Context context;
    private Boolean isClicked = false;
    private RecyclerViewAddFriendClickListener mListener;

    public SearchAdapter(ArrayList<SearchedUsersModel> searchedUsersModelArrayList, Context context, RecyclerViewAddFriendClickListener mListener) {
        this.searchedUsersModelArrayList = searchedUsersModelArrayList;
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_user_list_items, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchedUsersModel item = searchedUsersModelArrayList.get(position);
        String user_name = item.getName();
        String sex = item.getGender();
        String secret_id = item.getSecret_id();
        String user_image = item.getImage();

        holder.searched_user_name.setText(user_name);
        holder.searched_user_gender.setText(sex);
        holder.searched_user_secretKey.setText(secret_id);
        //  holder.searched_user_mobile.setText(mobile_no);
        Glide.with(context).load(user_image).into(holder.searched_user_image);

        if (searchedUsersModelArrayList.get(position).getStatus().equals("")) {
            holder.cancle_friend_request.setVisibility(View.GONE);
            holder.send_friend_request.setVisibility(View.VISIBLE);
        } else if (searchedUsersModelArrayList.get(position).getStatus().equals("pending")) {
            holder.cancle_friend_request.setVisibility(View.VISIBLE);
            holder.send_friend_request.setVisibility(View.GONE);

        } else {
            holder.cancle_friend_request.setVisibility(View.GONE);
            holder.send_friend_request.setVisibility(View.GONE);

        }


        // Toast.makeText(context, ""+user_name, Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return searchedUsersModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView searched_user_image;
        public TextView searched_user_secretKey, searched_user_name, searched_user_gender;

        public Button send_friend_request, cancle_friend_request;
        private RecyclerViewAddFriendClickListener mListener;

        public ViewHolder(final View itemView, RecyclerViewAddFriendClickListener recyclerViewAddFriendClickListener) {
            super(itemView);
            this.mListener = recyclerViewAddFriendClickListener;

            searched_user_secretKey = (TextView) itemView.findViewById(R.id.searched_user_secretKey);
            searched_user_gender = (TextView) itemView.findViewById(R.id.searched_user_gender);

            searched_user_image = (CircleImageView) itemView.findViewById(R.id.searched_user_image);
            searched_user_name = (TextView) itemView.findViewById(R.id.searched_user_name);
            send_friend_request = (Button) itemView.findViewById(R.id.send_friend_request);
            cancle_friend_request = (Button) itemView.findViewById(R.id.cancle_friend_request);
            // searched_user_mobile = (TextView) itemView.findViewById(R.id.searched_user_mobile);

            send_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancle_friend_request.setVisibility(View.VISIBLE);
                    if (!mListener.equals(null)) {
                        mListener.onAddFriend(v, getAdapterPosition());
                    }
                    //   Toast.makeText(v.getContext(), ""+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            cancle_friend_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCancleFriendRequest(v, getAdapterPosition());
                    cancle_friend_request.setVisibility(View.GONE);
                    send_friend_request.setVisibility(View.VISIBLE);
                }
            });

        }
    }
}