package com.example.hp.chatapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hp.chatapplication.Adapter.FriendListAdapter;
import com.example.hp.chatapplication.Adapter.SearchAdapter;
import com.example.hp.chatapplication.ForgotPasswordActivity;
import com.example.hp.chatapplication.Intefaces.FriendListInterface;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.Main2Activity;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.ModelClasses.SearchedUsersModel;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.UserNavgation;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBirdException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsListFragments extends Fragment {


    String userId;
    ArrayList<FriendListModel>friendListModelArrayList;
    FriendListAdapter friendListAdapter;
    RecyclerView  rv_friendList;

    public FriendsListFragments() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userId  = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        View view= inflater.inflate(R.layout.fragment_friends_list_fragments, container, false);
        loadAllFriends();

        rv_friendList = (RecyclerView) view.findViewById(R.id.rv_friendList);
        rv_friendList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_friendList.setLayoutManager(layoutManager);


        return view;

    }

    private void loadAllFriends() {

        final String LOGIN_URL= BaseUrl_ConstantClass.BASE_URL;
        friendListModelArrayList=new ArrayList<>();

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray= jsonObject.getJSONArray("search_result");
                            for (int i=0;i<=jsonArray.length();i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                String name = user_details.optString("name");
                                String secrate_id = user_details.optString("secrate_id");
                                String user_image = user_details.optString("user_img");
                                String gender=user_details.optString("gender");
                                String user_id=user_details.optString("userid");

                                FriendListModel friendListModel = new FriendListModel(name,user_image,gender,secrate_id,user_id);
                                //adding the hero to searchedlIst
                                friendListModelArrayList.add(friendListModel);

                                 friendListAdapter= new FriendListAdapter(friendListModelArrayList, getActivity(), new FriendListInterface() {
                                     @Override
                                     public void messageFriend(View view, int position) {
                                         FriendListModel friendListModel =        friendListModelArrayList.get(position);
                                         message(friendListModel);

                                     }

                                     @Override
                                     public void unfriedFriends(View view, int position) {


                                     }

                                 });
                                 rv_friendList.setAdapter(friendListAdapter);
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), ""+getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), ""+getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), ""+getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }                                  }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "getfriendlist");
                logParams.put("userid", userId);
                logParams.put("requesttype","accepted");

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    public void message( FriendListModel friendListModel)
    {
        List<String> users = new ArrayList<>();
        users.add(friendListModel.getSecret_id());
        users.add(userId);


        /*List<String> operators = new ArrayList<>();
        operators.add("Jeff");*/

        GroupChannelParams params = new GroupChannelParams()
                .setPublic(false)
                .setEphemeral(false)
                .setDistinct(false)
                .addUserIds(users)
                .setName(friendListModel.getName())
                .setCoverUrl(friendListModel.getImage());

        GroupChannel.createChannel(params, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }

                Intent intent = new Intent(getActivity(), Main2Activity.class);
                intent.putExtra("URL", groupChannel.getUrl());
                startActivity(intent);
            }
        });
    }
}
