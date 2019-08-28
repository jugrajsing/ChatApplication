package com.example.hp.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Fragments.CallFragment;
import com.example.hp.chatapplication.Fragments.ChatFragment;
import com.example.hp.chatapplication.Fragments.EventsFragment;
import com.example.hp.chatapplication.Fragments.ScheduleFragment;
import com.example.hp.chatapplication.Fragments.SearchFragments;
import com.example.hp.chatapplication.Fragments.SocialFragment;
import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.example.hp.chatapplication.VideoCall.utils.SharedPrefsHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserNavgation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    protected OnBackPressedListener onBackPressedListener;
    CircleImageView user_imageView_nav;
    TabLayout tabLayout;
    ImageButton search_button_chat;
    TextView user_name_id_nav, user_email_nav, user_resident;
    private SharedPrefsHelper sharedPrefsHelper;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadImage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        connectsendBirds();
        Fragment fragment = new ChatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        setContentView(R.layout.activity_user_navgation);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        search_button_chat = (ImageButton) findViewById(R.id.search_button_chat);
        search_button_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserNavgation.this, "search", Toast.LENGTH_SHORT).show();
                Fragment fragment = new SearchFragments();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //    toolbar.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //find here nevigation header
        View headerview = navigationView.getHeaderView(0);
        user_imageView_nav = headerview.findViewById(R.id.user_imageView_nav);//find the navigation header

        user_name_id_nav = (TextView) headerview.findViewById(R.id.user_name_id_nav);
        String secret_id = SharedPrefManager.getInstance(UserNavgation.this).getUser().getUser_name().toString();
        user_name_id_nav.setText(secret_id);

        user_email_nav = (TextView) headerview.findViewById(R.id.user_email_nav);
        String email = SharedPrefManager.getInstance(UserNavgation.this).getUser().getUser_secret_id().toString();
        user_email_nav.setText(email);


        user_resident = (TextView) headerview.findViewById(R.id.user_resident);
        String address = SharedPrefManager.getInstance(UserNavgation.this).getUser().getResident().toString();
        user_resident.setText(address);

      /*  user_resident=(TextView) headerview.findViewById(R.id.user_resident);

        SharedPreferences prefs = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        }
        String restoredText = prefs.getString("residentid", null);
        if (restoredText != null) {
            String name = prefs.getString("residentid", "No name defined");//"No name defined" is the default value.
            user_resident.setText(name);

        }*/
      /*  if (resident.equals(null)){
            user_resident.setText(" ");
        }
        else {
            user_resident.setText(resident);
        }
*/
        user_imageView_nav.setOnClickListener(this);
        includetabs();
        loadImage();
    }

    private void connectsendBirds() {
        //    String secret_id=SharedPrefManager.getInstance(MainActivity.this).getUser().getUser_secret_id().toString();

        String userId = SharedPrefManager.getInstance(UserNavgation.this).getUser().getUser_id().toString();
        if (sharedPrefsHelper.hasQbUser())
            userId = String.valueOf(sharedPrefsHelper.getQbUser().getId());
        userId = userId.replaceAll("\\s", "");

        String userNickname = SharedPrefManager.getInstance(UserNavgation.this).getUser().getUser_name().toString();
        connectToSendBird(userId, userNickname);
    }

    private void connectToSendBird(String userId, final String userNickname) {

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(com.sendbird.android.User user, SendBirdException e) {
                if (e != null) {
                    // Error!
                    //    Toast.makeText(MainActivity.this, "" + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Show login failure snackbar
                    //  showSnackbar("Login to SendBird failed");
                    //   mConnectButton.setEnabled(true);
                    //    PreferenceUtils.setConnected(MainActivity.this, false);
                    return;
                }
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();
            }

        });
    }


    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */


    /**
     * Update the user's push token.
     * /*
     */
    private void updateCurrentUserPushToken() {
        // Register Firebase Token
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
                new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            //          Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //    Toast.makeText(MainActivity.this, "Push token registered.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
//               //     Toast.makeText(
//                            MainActivity.this, "" + e.getCode() + ":" + e.getMessage(),
//                            Toast.LENGTH_SHORT)
//                            .show();

                    // Show update failed snackbar
                    //  showSnackbar("Update user nickname failed");

                    return;
                }

            }
        });
    }



  /*  @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_navgation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(UserNavgation.this, UserDetailsActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_dashboard) {
            Fragment fragment = new ChatFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();


        } else if (id == R.id.nav_posts) {
            Fragment fragment = new SocialFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_findfriends) {
            Intent intent = new Intent(UserNavgation.this, FriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share_me) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Buzz App Here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

        } else if (id == R.id.nav_frame) {

            Intent intent = new Intent(UserNavgation.this, UserDetailsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(UserNavgation.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {

            Intent intent = new Intent(UserNavgation.this, HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.user_imageView_nav: {
                startActivity(new Intent(UserNavgation.this, UserDetailsActivity.class));

            }
        }
    }

    private void includetabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_black));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_white));
        tabLayout.addTab(tabLayout.newTab().setText("Call"));
        tabLayout.addTab(tabLayout.newTab().setText("Social"));
        tabLayout.addTab(tabLayout.newTab().setText("Event"));
        tabLayout.addTab(tabLayout.newTab().setText("Timer"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new CallFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new SocialFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 3) {
                    //   Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new EventsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack
                            (null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 4) {
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new ScheduleFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack
                            (null).commit();
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

    private void loadImage() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        final String user_id = SharedPrefManager.getInstance(UserNavgation.this).getUser().getUser_id().toString();


        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("success");
                            String message = jsonObject.optString("message");
                            String user_image = jsonObject.optString("user_img");
                            if (status.equals("true")) {
                                // Toast.makeText(UserNavgation.this, ""+message, Toast.LENGTH_SHORT).show();
                                if (user_image != null) {
                                    Glide.with(UserNavgation.this).load(user_image).into(user_imageView_nav);
                                } else {
                                    user_imageView_nav.setBackgroundResource(R.drawable.app_buzz_logo);

                                }
                            } else {
                                Toast.makeText(UserNavgation.this, "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserNavgation.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(UserNavgation.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(UserNavgation.this, "" + getString(R.string.error_network_timeout),
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
                logParams.put("action", "getimg");
                logParams.put("userid", user_id);
                return logParams;
            }
        };

        MySingleTon.getInstance(UserNavgation.this).addToRequestQue(stringRequestLogIn);

    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    /* @Override
     public boolean onKeyDown(int keyCode, KeyEvent event)
     {
         if ((keyCode == KeyEvent.KEYCODE_BACK))
         {

             if (onBackPressedListener != null) {
                 onBackPressedListener.doBack();
             }
             else {
                 finish();
             }
         }
         return super.onKeyDown(keyCode, event);
     }*/
    private void logout() {

        if (SharedPrefManager.getInstance(UserNavgation.this).isLoggedIn() == false) {
            startActivity(new Intent(UserNavgation.this, LoginRegistrationActivity.class));
            finish();
        } else {
            SharedPrefManager.getInstance(UserNavgation.this).logout();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (onBackPressedListener != null) {
                onBackPressedListener.doBack();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            // }else

            //this.moveTaskToBack(true);
        } else if (onBackPressedListener != null) {
            onBackPressedListener.doBack();

        } else {
            super.onBackPressed();
            //this.moveTaskToBack(true);
            //getFragmentManager().popBackStack();
            //onBackPressedListener.doBack();
        }

   /* @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)

        else
            super.onBackPressed();
    }*/
    }
}