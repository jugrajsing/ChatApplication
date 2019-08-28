package com.example.hp.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hp.chatapplication.Fragments.GroupChatFragment;
import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;


public class Main2Activity extends AppCompatActivity {

    protected OnBackPressedListener onBackPressedListener;
    String channelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        channelUrl = getIntent().getStringExtra("URL");

        GroupChatFragment fragment = GroupChatFragment.newInstance(channelUrl);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_group_channel, fragment)
                .addToBackStack(null)
                .commit();
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }
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

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
