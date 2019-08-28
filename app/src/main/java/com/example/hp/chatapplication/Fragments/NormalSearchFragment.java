package com.example.hp.chatapplication.Fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.hp.chatapplication.Adapter.SearchAdapter;
import com.example.hp.chatapplication.Intefaces.RecyclerViewAddFriendClickListener;
import com.example.hp.chatapplication.ModelClasses.SearchedUsersModel;
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
public class NormalSearchFragment extends Fragment {

    RecyclerView rv_user_list;
    RecyclerView.Adapter adapter;
    SearchAdapter searchingDetailsAdapter;
    ArrayList<SearchedUsersModel> searchedUsersModelArrayList;
    EditText et_search_friends;
    String friend_id,user_id;
    LinearLayout linear_search_list;

    public NormalSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_search, container, false);
        user_id = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        linear_search_list = (LinearLayout) view.findViewById(R.id.linear_search_list);
        et_search_friends = (EditText) view.findViewById(R.id.et_search_friends);
        loadSearchedDetails(et_search_friends.getText().toString());
        et_search_friends.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadSearchedDetails(et_search_friends.getText().toString());
            }
        });

        rv_user_list = (RecyclerView) view.findViewById(R.id.rv_user_list);
        rv_user_list.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_user_list.setLayoutManager(layoutManager);

        return view;

    }

    private void loadSearchedDetails(final String keyWord) {

        final String LOGIN_URL= BaseUrl_ConstantClass.BASE_URL;
        searchedUsersModelArrayList=new ArrayList<>();
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
                              //  Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                                String user_img=user_details.optString("user_img");
                                String mobileno = user_details.optString("mobileno");
                                String f_id=user_details.optString("frnid");
                                String gender = user_details.optString("gender");
                                String secret_id = user_details.optString("secrate_id");
                                String friend_status=user_details.optString("frnstatus");

                                SearchedUsersModel searchedDetails = new SearchedUsersModel(name,user_img,mobileno,gender,secret_id,friend_status,f_id);
                                //adding the hero to searchedlIst
                                searchedUsersModelArrayList.add(searchedDetails);
                                searchingDetailsAdapter= new SearchAdapter(searchedUsersModelArrayList, getContext(), new RecyclerViewAddFriendClickListener() {
                                    @Override
                                    public void onAddFriend(View view, int position) {
                                        if (!searchedUsersModelArrayList.isEmpty()) {
                                            friend_id = searchedUsersModelArrayList.get(position).getFriend_id();
                                            sendFriendRequest();
                                        }
                                    }
                                    @Override
                                    public void onCancleFriendRequest(View view, int position) {
                                        cancleFriendRequest();
                                    }
                                });
                                rv_user_list.setAdapter(searchingDetailsAdapter);

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
                logParams.put("action", "searchfriends");
                logParams.put("searchkey", keyWord);
                logParams.put("userid",user_id);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void sendFriendRequest() {

        final String LOGIN_URL= BaseUrl_ConstantClass.BASE_URL;
        searchedUsersModelArrayList=new ArrayList<>();

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
                                Toast.makeText(getActivity(), "Success"+ message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
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
                logParams.put("action", "friendrequest");
                logParams.put("userid", user_id);
                logParams.put("frnid", friend_id);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void cancleFriendRequest() {

        final String LOGIN_URL= BaseUrl_ConstantClass.BASE_URL;
        searchedUsersModelArrayList=new ArrayList<>();

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
                                Toast.makeText(getActivity(), "Success"+ message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
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
                logParams.put("action","removefriend");
                logParams.put("userid",user_id);
                logParams.put("frnid", friend_id);
                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }
}
