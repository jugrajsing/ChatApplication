package com.example.hp.chatapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hp.chatapplication.VideoCall.services.CallService;
import com.example.hp.chatapplication.VideoCall.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;


public class SplashScreen extends AppCompatActivity {

    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        if(!isConnected(SplashScreen.this)) buildDialog(SplashScreen.this).show();
        else {
            //Toast.makeText(SplashScreen.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_splash_screen);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (sharedPrefsHelper.hasQbUser()) {
                        startLoginService(sharedPrefsHelper.getQbUser());
                        //     ();
                        startActivity(new Intent(SplashScreen.this, LoginRegistrationActivity.class));
                        finish();
                        return;
                    }
                    else {
                        startActivity(new Intent(SplashScreen.this, LoginRegistrationActivity.class));
                        finish();
                    }


                }
            },4000);

            }

    }

    protected void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();


        if (netinfo != null && netinfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;

        } else

            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
