package com.example.hp.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class HelpActivity extends AppCompatActivity {

    private ImageView iv_back_to_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        iv_back_to_friends = findViewById(R.id.iv_back_to_friends);
        iv_back_to_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
