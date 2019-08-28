package com.example.hp.chatapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.example.hp.chatapplication.Fragments.FriendRequestFragment;
import com.example.hp.chatapplication.Fragments.FriendsListFragments;
import com.example.hp.chatapplication.ModelClasses.FriendListModel;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView iv_back_to_friends;
    String user_id;

    ArrayList<FriendListModel> searchedUsersModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        user_id = SharedPrefManager.getInstance(this).getUser().getUser_id().toString();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        iv_back_to_friends = (ImageView) findViewById(R.id.iv_back_to_friends);
        iv_back_to_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        includetabs();
    }

    private void includetabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Friend"));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_black));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_white));
        tabLayout.addTab(tabLayout.newTab().setText("Friend Request"));


        Fragment fragment = new FriendsListFragments();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new FriendsListFragments();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new FriendRequestFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void cancleFriendRequest() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        searchedUsersModelArrayList = new ArrayList<>();

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
                                Toast.makeText(FriendsActivity.this, "Success" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FriendsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FriendsActivity.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FriendsActivity.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FriendsActivity.this, "" + getString(R.string.error_network_timeout),
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
                logParams.put("userid", user_id);
                logParams.put("frnid", "");

                return logParams;
            }
        };

        MySingleTon.getInstance(FriendsActivity.this).addToRequestQue(stringRequestLogIn);

    }
}
