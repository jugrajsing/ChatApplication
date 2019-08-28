package com.example.hp.chatapplication.QuickBlox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.chatapplication.R;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class QuickBloxLogin extends AppCompatActivity {

    static final String APP_ID = "73990";
    static final String AUTH_KEY = "grphy3MAP78hCJQ";
    static final String AUTH_SECRET = "qGVnSv4RAgduxK7";
    static final String ACCOUNT_KEY = "WsfPck-2axeKWBZ9sw4X";


    Button btn_login,btn_signup;
    EditText et_login,et_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_blox_login);
        initializeFramework();

        et_login=(EditText) findViewById(R.id.et_login);
        et_pass=(EditText) findViewById(R.id.et_pass);

        btn_login=(Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name=et_login.getText().toString();
                final String password=et_pass.getText().toString();

                QBUser user = new QBUser(user_name,password);

                QBUsers.signIn(user).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle params) {
                        Toast.makeText(QuickBloxLogin.this, "Success full Sign in ", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(QuickBloxLogin.this, ChatDialogsActivity.class);
                        intent.putExtra("user",user_name);
                        intent.putExtra("password",password);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(QBResponseException errors) {
                        Toast.makeText(QuickBloxLogin.this, ""+ errors.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_signup=(Button)findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuickBloxLogin.this, QuickBloxSignUp.class));
            }
        });

    }

    private void initializeFramework() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

}
