package com.example.hp.chatapplication.QuickBlox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.chatapplication.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class QuickBloxSignUp extends AppCompatActivity {

    Button button_signUp,button_signUp_cancel;
    EditText et_user_name_signup,et_pass_signup,et_full_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_blox_sign_up);
        
        registerSession();
        
        et_user_name_signup=(EditText) findViewById(R.id.et_user_name_signup);
        et_pass_signup=(EditText) findViewById(R.id.et_pass_signup);
        et_full_name=(EditText) findViewById(R.id.et_full_name);
        button_signUp_cancel=(Button)findViewById(R.id.button_signUp_cancel);


        button_signUp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_signUp=(Button)findViewById(R.id.button_signUp);
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name=et_user_name_signup.getText().toString();
                String password=et_pass_signup.getText().toString();


                QBUser qbUser = new QBUser(user_name,password);
                qbUser.setFullName(et_full_name.getText().toString());


                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(QuickBloxSignUp.this, "Signup success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(QuickBloxSignUp.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void registerSession() {

        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
            }

            @Override
            public void onError(QBResponseException error) {
                Log.e("ERROR", error.getMessage());
                // errors
            }
        });
    }


}
