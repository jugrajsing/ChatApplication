package com.example.hp.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.hp.chatapplication.ModelClasses.User;

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "current_user";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String SECRET_ID = "secret_id";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_WORK = "work";
    private static final String KEY_RESIDENT = "resident";
    private static final String KEY_MARTIAL = "martial";
    private static final String KEY_SECURITY = "security";
    private static final String KEY_INTEREST = "interest";
    private static final String KEY_USER_IMAGE = "user_image";
    private static final String KEY_CONNECTED = "connected";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, user.getUser_id());
        editor.putString(KEY_USERNAME, user.getUser_name());
        editor.putString(KEY_EMAIL, user.getUser_email());
        editor.putString(KEY_MOBILE, user.getUser_mobile_no());
        editor.putString(PASSWORD, user.getUser_password());
        editor.putString(SECRET_ID, user.getUser_secret_id());
        editor.putString(KEY_USER_IMAGE, user.getUser_image());
        editor.putString(KEY_RESIDENT, user.getResident());
        editor.putString(KEY_CONNECTED, user.getConnected());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null) != null;
    }


    public String isConnected() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CONNECTED, null);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USER_ID, null),//user id
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(PASSWORD, null),
                sharedPreferences.getString(SECRET_ID, null),
                sharedPreferences.getString(KEY_USER_IMAGE, null),
                sharedPreferences.getString(KEY_RESIDENT, null),
                sharedPreferences.getString(KEY_CONNECTED, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginRegistrationActivity.class));
    }
}