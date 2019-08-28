package com.example.hp.chatapplication.ModelClasses;


import android.app.Application;

import com.example.hp.chatapplication.R;
import com.sendbird.android.SendBird;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes(mailTo = "viveksingh0301@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text
)
public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SendBird.init("5B6AFD68-5982-4426-8E51-D6C0B8C42FA5", getApplicationContext());
        //ACRA.init(this);

    }
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }
}
