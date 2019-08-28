package com.example.hp.chatapplication.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.openchannel.OpenChatFragment;

public class Main3Activity extends AppCompatActivity {
    protected OnBackPressedListener onBackPressedListener;
    String channelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelUrl = getIntent().getStringExtra("URL");
        setContentView(R.layout.activity_main3);

        OpenChatFragment frag = OpenChatFragment.newInstance(channelUrl);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_open_channel, frag)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_open_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);

        }
    }

    /*   @Override
       public boolean onKeyDown(int keyCode, KeyEvent event)
       {
           if ((keyCode == KeyEvent.KEYCODE_BACK))
           {
               finish();
           }
           return super.onKeyDown(keyCode, event);
       }*/
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

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
