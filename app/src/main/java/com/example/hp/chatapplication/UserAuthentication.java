package com.example.hp.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

public class UserAuthentication extends AppCompatActivity {

    private final static int REQUEST_CODE = 999;
    Button phone_login, email_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);
        email_login = (Button) findViewById(R.id.email_login);
        phone_login = (Button) findViewById(R.id.phone_login);

        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.EMAIL);
            }
        });

        phone_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

    }


    private void startLoginPage(LoginType loginType) {
        if (loginType == LoginType.EMAIL) {
            final Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN);
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        } else if (loginType == LoginType.PHONE) {
            final Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (result.getAccessToken() != null)
                    Toast.makeText(this, "Success ! %s" + result.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Success ! %s" + result.getAuthorizationCode().substring(0, 10), Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this,Success.class));
                startActivity(new Intent(this, MainActivity.class));
            }

        }
    }


}
