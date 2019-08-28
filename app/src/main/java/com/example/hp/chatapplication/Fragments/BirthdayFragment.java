package com.example.hp.chatapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.example.hp.chatapplication.Adapter.BirthdayAdapter;
import com.example.hp.chatapplication.Adapter.PostListAdapter;
import com.example.hp.chatapplication.ModelClasses.BirthdayModel;
import com.example.hp.chatapplication.ModelClasses.PostListModel;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BirthdayFragment extends Fragment {


    RecyclerView birthday_Recycler;
    ArrayList<BirthdayModel> birthdayModelArrayList;
    BirthdayAdapter birthdayAdapter;
    String userId;
    ProgressBar birthday_progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_birthday, container, false);
        userId  = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        birthday_Recycler=(RecyclerView) view.findViewById(R.id.birthday_Recycler);
        birthday_progress=(ProgressBar) view.findViewById(R.id.birthday_progress);
        loadBirthdays();
        birthday_Recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        birthday_Recycler.setLayoutManager(layoutManager);
        return view;

    }


    private void loadBirthdays()
    {
        birthday_progress.setVisibility(View.VISIBLE);
        final String POST_URL= BaseUrl_ConstantClass.BASE_URL+ "?";
        birthdayModelArrayList=new ArrayList<>();
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            birthday_progress.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray= jsonObject.getJSONArray("search_result");
                            for (int i=0;i<=jsonArray.length();i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                String name = user_details.optString("name");
                                String date_of_birth = user_details.optString("dob");
                                String user_image = user_details.optString("user_img");

                                BirthdayModel birthdayModel = new BirthdayModel(name,date_of_birth,user_image);
                                //adding the hero to searchedlIst
                                birthdayModelArrayList.add(birthdayModel);

                                birthdayAdapter= new BirthdayAdapter(getActivity(),birthdayModelArrayList);
                                birthday_Recycler.setAdapter(birthdayAdapter);
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
                        }                              }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action","getfriendbirthdays");
                logParams.put("userid",userId);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }
}
