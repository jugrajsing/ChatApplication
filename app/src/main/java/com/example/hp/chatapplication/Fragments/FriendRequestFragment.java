package com.example.hp.chatapplication.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.hp.chatapplication.Adapter.FriendRequestAdapter;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequestFragment extends Fragment {


    String userId;
    ArrayList<FriendListModel> friendRequestAdapterArrayList;
    FriendRequestAdapter friendRequestAdapter;
    RecyclerView rv_friend_request;
    String frnid_id;
    private ImageView noData;

    public FriendRequestFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userId = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        loadAllFriends();

        rv_friend_request = (RecyclerView) view.findViewById(R.id.rv_friend_request);
//        noData = view.findViewById(R.id.noData);
        rv_friend_request.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_friend_request.setLayoutManager(layoutManager);


        return view;

    }

    private void loadAllFriends() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        friendRequestAdapterArrayList = new ArrayList<>();

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("search_result");
                            if(jsonArray.length()>0){
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                String name = user_details.optString("name");
                                String secrate_id = user_details.optString("secrate_id");
                                String user_image = user_details.optString("user_img");
                                String gender = user_details.optString("gender");
                                String user_id = user_details.optString("userid");
                                FriendListModel friendListModel = new FriendListModel(name, user_image, gender, secrate_id, user_id);
                                //adding the hero to searchedlIst
                                friendRequestAdapterArrayList.add(friendListModel);

                                friendRequestAdapter = new FriendRequestAdapter(friendRequestAdapterArrayList, getActivity(), new RecyclerViewAddFriendClickListener() {
                                    @Override
                                    public void onAddFriend(View view, int position) {
                                        frnid_id = friendRequestAdapterArrayList.get(position).getUser_id();
                                        friendRequestAdapterArrayList.remove(position);
                                        friendRequestAdapter.notifyDataSetChanged();

                                        Accept();
                                    }

                                    @Override
                                    public void onCancleFriendRequest(View view, int position) {
                                        frnid_id = friendRequestAdapterArrayList.get(position).getUser_id();
                                        friendRequestAdapterArrayList.remove(position);
                                        friendRequestAdapter.notifyDataSetChanged();
                                        cancleFriendRequest();

                                    }
                                });
                                rv_friend_request.setAdapter(friendRequestAdapter);
                            }}else {
                                noData.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            noData.setVisibility(View.VISIBLE);
                            noData.setImageResource(R.drawable.no_internet_);
                            noData.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "getfriendlist");
                logParams.put("userid", userId);
                logParams.put("requesttype", "pending");

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void cancleFriendRequest() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        friendRequestAdapterArrayList = new ArrayList<>();

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (status.equals("true")) {
                                Toast.makeText(getActivity(), "Success" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            noData.setVisibility(View.VISIBLE);
                            noData.setImageResource(R.drawable.no_internet_);
                            noData.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "removefriend");
                logParams.put("userid", userId);
                logParams.put("frnid", frnid_id);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void Accept() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        friendRequestAdapterArrayList = new ArrayList<>();

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (status.equals("true")) {
                                Toast.makeText(getActivity(), "Success" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            noData.setVisibility(View.VISIBLE);
                            noData.setImageResource(R.drawable.no_internet_);
                            noData.setScaleType(ImageView.ScaleType.CENTER_INSIDE);} else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "acceptrequest");
                logParams.put("userid", userId);
                logParams.put("frnid", frnid_id);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }


}
