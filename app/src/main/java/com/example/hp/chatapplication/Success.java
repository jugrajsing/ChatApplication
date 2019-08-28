package com.example.hp.chatapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class Success extends AppCompatActivity {
    Button logout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        logout_button=(Button) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AccountKit.logOut();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {

            @Override
            public void onSuccess(Account account) {
                EditText user_id,phone_user,email_user;

                user_id=(EditText) findViewById(R.id.user_id);
                user_id.setText(String.format("User ID is %s", account.getId()));

                email_user=(EditText) findViewById(R.id.email_user);
                email_user.setText(String.format("Email is %s", account.getEmail()));

                phone_user=(EditText) findViewById(R.id.phone_user);
                phone_user.setText(String.format("Phone is %s", account.getPhoneNumber()==null ? "" :account.getPhoneNumber().toString()));


            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }
}
