package com.example.hp.chatapplication.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.hp.chatapplication.Adapter.AdvanceSearchAdapter;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
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
public class AdvanceSearchFragment extends Fragment {


    String userId;
    ArrayList<FriendListModel> friendListModelArrayList;
    // FriendListAdapter friendListAdapter;
    AdvanceSearchAdapter advanceSearchAdapter;

    RecyclerView rv_advance_search;

    EditText et_company_name, et_interests;
    String company_name, interest;
    Button search;


    public AdvanceSearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //userId  = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        View view = inflater.inflate(R.layout.fragment_advance_search, container, false);


        et_company_name = (EditText) view.findViewById(R.id.et_company_name);
        et_interests = (EditText) view.findViewById(R.id.et_interests);

        search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllFriends(et_company_name.getText().toString(), et_interests.getText().toString());
            }
        });

        rv_advance_search = (RecyclerView) view.findViewById(R.id.rv_advance_search);
        rv_advance_search.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_advance_search.setLayoutManager(layoutManager);

        return view;

    }

    private void loadAllFriends(final String company_name, final String interests) {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        friendListModelArrayList = new ArrayList<>();

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("search_result");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                String name = user_details.optString("name");
                                String secrate_id = user_details.optString("secrate_id");
                                String user_image = user_details.optString("user_img");
                                String gender = user_details.optString("gender");
                                String user_id = user_details.optString("userid");

                                FriendListModel friendListModel = new FriendListModel(name, user_image, gender, secrate_id, user_id);
                                //adding the hero to searchedlIst
                                friendListModelArrayList.add(friendListModel);

                                advanceSearchAdapter = new AdvanceSearchAdapter(friendListModelArrayList, getActivity());
                                rv_advance_search.setAdapter(advanceSearchAdapter);

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
                logParams.put("action", "searchfriendsadvance");
                logParams.put("company", company_name);
                logParams.put("intrest", interests);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }
}
