package com.example.hp.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.chatapplication.Adapter.BuzzAdapter;

public class ChatScreenBuzzActivity extends AppCompatActivity {

    RecyclerView buzz_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_buzz);
        init();

    }

    private void init() {
        buzz_recycler = (RecyclerView) findViewById(R.id.buzz_recycler);
        buzz_recycler.setLayoutManager(new LinearLayoutManager(ChatScreenBuzzActivity.this));
        buzz_recycler.setHasFixedSize(true);
        BuzzAdapter buzzAdapter = new BuzzAdapter(ChatScreenBuzzActivity.this);
        buzz_recycler.setAdapter(buzzAdapter);

    }
}

