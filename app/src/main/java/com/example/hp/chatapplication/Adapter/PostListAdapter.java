package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Intefaces.LikeInterface;
import com.example.hp.chatapplication.ModelClasses.PostListModel;
import com.example.hp.chatapplication.R;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>
{
    ArrayList<PostListModel> friendListModelArrayList;
    private Context context;

    LikeInterface likeInterface;

    public PostListAdapter(ArrayList<PostListModel> friendListModelArrayList, Context context,LikeInterface likeInterface) {
        this.friendListModelArrayList = friendListModelArrayList;
        this.context = context;
        this.likeInterface=likeInterface;
    }


    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postlist1 , parent, false);
        return new PostListAdapter.ViewHolder(v,likeInterface);    }

    @Override
    public void onBindViewHolder(@NonNull final PostListAdapter.ViewHolder holder, int position)
    {
        PostListModel postListModel=friendListModelArrayList.get(position);
//        holder.postTitle.setText(postListModel.getPost_title());
        holder.postContent.setText(postListModel.getContent());
        holder.postBy.setText(postListModel.getPostedby());
        holder.tv_date.setText(postListModel.getPosted_date());
        holder.tv_postlike.setText(postListModel.getPost_likes());

        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.iv_more);

                //inflating menu from xml resource
                popup.inflate(R.menu.more_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.friendrequest:
                                //handle menu1 click
                                return true;
                            case R.id.turnonoff_notification:
                                //handle menu2 click
                                return true;
                          /*  case R.id.tv_hideallposts:
                                //handle menu3 click
                                return true;*/
                            case R.id.unfollow:
                                //handle menu3 click
                                return true;
                            case R.id.reportpost:
                                //handle menu3 click
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

        if (postListModel.getYou_liked().contains("Yes")){
            holder.iv_like.setImageResource(R.mipmap.ic_heart_red);
        }
        else
            {
                holder.iv_like.setImageResource(R.mipmap.ic_heart_white);

        }

       /* if (postListModel.getIv_deslike().contains("Yes")){
            holder.iv_deslike.setImageResource(R.drawable.ic_deslike);
        }
        else
        {
            holder.iv_deslike.setImageResource(R.mipmap.ic_heart_white);

        }
*/

        Glide.with(context).load(postListModel.getPost_profileimg()).into( holder.profile_img);
       /* holder.videoView.setVideoURI(Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        holder.videoView.start();*/

    }













    @Override
    public int getItemCount() {
        return friendListModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView postContent,postBy , tv_date, tv_postlike;
        private ImageView iv_like,profile_img,iv_more,iv_deslike;
        LikeInterface likeInterface;
        VideoView videoView;


        public ViewHolder(final View itemView, final LikeInterface likeInterface) {
            super(itemView);
            this.likeInterface=likeInterface;

  //          postTitle=(TextView) itemView.findViewById(R.id.postTitle);
              postContent=(TextView) itemView.findViewById(R.id.postContent);
              postBy=(TextView) itemView.findViewById(R.id.postBy);
              tv_date=(TextView) itemView.findViewById(R.id.date);
              tv_postlike=(TextView)itemView.findViewById(R.id.tv_likes);
              profile_img=(ImageView)itemView.findViewById(R.id.post_profile_img);
              iv_like=(ImageView)itemView.findViewById(R.id.iv_likes);
              iv_deslike=(ImageView)itemView.findViewById(R.id.iv_deslike);
              iv_more=(ImageView)itemView.findViewById(R.id.iv_more);
            videoView=(VideoView)itemView.findViewById(R.id.videoView);


            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeInterface.likePost(itemView ,getAdapterPosition());
                }
            });
        }
    }


    }
