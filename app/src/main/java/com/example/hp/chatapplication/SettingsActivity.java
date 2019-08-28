package com.example.hp.chatapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Switch friends_alert = null,chat_alert=null,group_alert=null;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        friends_alert = (Switch) findViewById(R.id.friends_alert);
        friends_alert.setOnCheckedChangeListener(this);

        chat_alert= (Switch) findViewById(R.id.chat_alert);
        chat_alert.setOnCheckedChangeListener(this);

        group_alert= (Switch) findViewById(R.id.group_alert);
        group_alert.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.friends_alert:
                // do your code
                if (isChecked) {
                    // do something when check is selected
                    friends_alert.setText("On");
                } else {
                    //do something when unchecked
                    friends_alert.setText("Off");

                }
                break;


            case R.id.chat_alert:
                // do your code

                if (isChecked) {
                    // do something when check is selected
                    chat_alert.setText("On");
                } else {
                    //do something when unchecked
                    chat_alert.setText("Off");
                    break;

                }

            case R.id.group_alert:
                if (isChecked) {
                    // do something when check is selected
                    group_alert.setText("On");
                } else {
                    //do something when unchecked
                    group_alert.setText("Off");
                    break;

                }

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.iv_back:
                finish();
                break;

        }
    }
}