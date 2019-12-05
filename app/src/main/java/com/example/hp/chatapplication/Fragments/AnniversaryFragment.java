package com.example.hp.chatapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.hp.chatapplication.Adapter.AnniversaryAdapter;
import com.example.hp.chatapplication.ModelClasses.AnniversaryModel;
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

public class AnniversaryFragment extends Fragment {

    RecyclerView anniversary_Recycler;
    ArrayList<AnniversaryModel> anniversaryModelArrayList;
    AnniversaryAdapter anniversaryAdapter;
    String userId;
    ProgressBar anniversary_progress;
    private ImageView noData;

    public AnniversaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anniversary, container, false);
        userId = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        anniversary_Recycler = (RecyclerView) view.findViewById(R.id.anniversary_Recycler);
        anniversary_progress = (ProgressBar) view.findViewById(R.id.anniversary_progress);
//        noData = view.findViewById(R.id.noData);
        loadAnniverSary();
        anniversary_Recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        anniversary_Recycler.setLayoutManager(layoutManager);
        return view;
    }

    private void loadAnniverSary() {
        anniversary_progress.setVisibility(View.VISIBLE);
        final String POST_URL = BaseUrl_ConstantClass.BASE_URL + "?";
        anniversaryModelArrayList = new ArrayList<>();
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            anniversary_progress.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("search_result");
                            if(jsonArray.length()>0) {
                                for (int i = 0; i <= jsonArray.length(); i++) {

                                    JSONObject user_details = jsonArray.getJSONObject(i);
                                    String name = user_details.optString("name");
                                    String anniversary = user_details.optString("anniversary");
                                    String user_image = user_details.optString("user_img");

                                    AnniversaryModel anniversaryModel = new AnniversaryModel(name, anniversary, user_image);
                                    //adding the hero to searchedlIst
                                    anniversaryModelArrayList.add(anniversaryModel);
                                    anniversaryAdapter = new AnniversaryAdapter(getActivity(), anniversaryModelArrayList);
                                    anniversary_Recycler.setAdapter(anniversaryAdapter);
                                }

                            }else {
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
                            noData.setScaleType(ImageView.ScaleType.CENTER_INSIDE);      } else if (error instanceof AuthFailureError) {
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
                logParams.put("action", "getfriendaniversary");
                logParams.put("userid", userId);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }


}
