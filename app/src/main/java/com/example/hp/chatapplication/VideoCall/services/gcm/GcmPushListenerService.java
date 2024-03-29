package com.example.hp.chatapplication.VideoCall.services.gcm;

import android.os.Bundle;
import android.util.Log;

import com.example.hp.chatapplication.VideoCall.services.CallService;
import com.example.hp.chatapplication.VideoCall.utils.SharedPrefsHelper;
import com.example.hp.chatapplication.VideoCall.utils.constant.GcmConsts;
import com.google.android.gms.gcm.GcmListenerService;
import com.quickblox.users.model.QBUser;

/**
 * Created by tereha on 13.05.16.
 */
public class GcmPushListenerService extends GcmListenerService {
    private static final String TAG = GcmPushListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString(GcmConsts.EXTRA_GCM_MESSAGE);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);

        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            Log.d(TAG, "App have logined user");
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            startLoginService(qbUser);
        }
    }

    private void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);
    }
}