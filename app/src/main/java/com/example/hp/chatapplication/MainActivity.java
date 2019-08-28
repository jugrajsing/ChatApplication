package com.example.hp.chatapplication;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.hp.chatapplication.Fragments.CallFragment;
import com.example.hp.chatapplication.Fragments.ChatFragment;
import com.example.hp.chatapplication.Fragments.EventsFragment;
import com.example.hp.chatapplication.Fragments.ScheduleFragment;
import com.example.hp.chatapplication.Fragments.SocialFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView iv_image_menu;
    CardView cv_chat,cv_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout= (TabLayout) findViewById(R.id.tabs);
        cv_chat=(CardView) findViewById(R.id.cv_chat);
       connectsendBirds();

       cv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();
                Fragment fragment=new ChatFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        cv_group=(CardView) findViewById(R.id.cv_group);
        cv_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Group", Toast.LENGTH_SHORT).show();
               // Fragment fragment=new GroupChatFragment();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
        iv_image_menu=(ImageView) findViewById(R.id.iv_image_menu);
        iv_image_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });

        loadFragment(new ChatFragment());

        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable( R.drawable.chatt)));
        tabLayout.getTabAt(0).setText("Chat");
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.color_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.app_color_pink));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable( R.drawable.call)));
        tabLayout.getTabAt(1).setText("Call");
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable( R.drawable.social)));
        tabLayout.getTabAt(2).setText("Social");
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable( R.drawable.event)));
        tabLayout.getTabAt(3).setText("Events");
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable( R.drawable.schedule)));
        tabLayout.getTabAt(4).setText("Schedule");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                  //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                    cv_chat.setVisibility(View.VISIBLE);
                    cv_group.setVisibility(View.VISIBLE);
                }
                else if(tabLayout.getSelectedTabPosition() == 1){
                   // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new CallFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                    cv_chat.setVisibility(View.GONE);
                    cv_group.setVisibility(View.GONE);
                }
                else if(tabLayout.getSelectedTabPosition() == 2){
                  //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new SocialFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                    cv_chat.setVisibility(View.GONE);
                    cv_group.setVisibility(View.GONE);
                }
                else if(tabLayout.getSelectedTabPosition() == 3){
                 //   Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new EventsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack
                            (null).commit();
                    cv_chat.setVisibility(View.GONE);
                    cv_group.setVisibility(View.GONE);
                }
                else if(tabLayout.getSelectedTabPosition() == 4){
                   // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new ScheduleFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack
                            (null).commit();
                    cv_chat.setVisibility(View.GONE);
                    cv_group.setVisibility(View.GONE);
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
    private void connectsendBirds() {
    //    String secret_id=SharedPrefManager.getInstance(MainActivity.this).getUser().getUser_secret_id().toString();

        String userId  =SharedPrefManager.getInstance(MainActivity.this).getUser().getUser_secret_id().toString();
        userId = userId.replaceAll("\\s", "");
        String userNickname =  SharedPrefManager.getInstance(MainActivity.this).getUser().getUser_name().toString();
        connectToSendBird(userId, userNickname);
    }

    private void connectToSendBird(String userId, final String userNickname) {

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                //  showProgressBar(false);

                if (e != null) {
                    // Error!
                    //    Toast.makeText(MainActivity.this, "" + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Show login failure snackbar
                    //  showSnackbar("Login to SendBird failed");
                    //   mConnectButton.setEnabled(true);
                //    PreferenceUtils.setConnected(MainActivity.this, false);
                    return;
                }

                //PreferenceUtils.setConnected(MainActivity.this, true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                // Proceed to MainActivity
               /* Intent intent = new Intent(MainActivity.this, MappingActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
    }
    /**
     * Update the user's push token.
     /*   */
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
     * @param userNickname  The new nickname of the user.
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


    private boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(fragment.getClass().getName()).commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      /*  Fragment fragment=new ChatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(fragment.getClass().getName()).commit();
*/
    }

    private void showPopUp(){
        PopupMenu popup = new PopupMenu(MainActivity.this, iv_image_menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if(SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()==false){
                    startActivity(new Intent(MainActivity.this,LoginRegistrationActivity.class));
                    finish();
                }
                else
                {
                    SharedPrefManager.getInstance(MainActivity.this).logout();
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }
}