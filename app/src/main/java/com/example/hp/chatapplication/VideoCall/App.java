package com.example.hp.chatapplication.VideoCall;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.example.hp.chatapplication.VideoCall.models.QbConfigs;
import com.example.hp.chatapplication.VideoCall.util.QBResRequestExecutor;
import com.example.hp.chatapplication.VideoCall.utils.ConfigUtils;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.sendbird.android.SendBird;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config_template.json";
    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;
    private QbConfigs qbConfigs;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initQBSessionManager();
        initQbConfigs();
        initCredentials();
        initApplication();
        SendBird.init("4FA24612-59FE-48F5-A78C-1405504CAE51", getApplicationContext());
    }

    private void initApplication() {
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    //////////////////////////

    private void initQbConfigs() {
        Log.e(TAG, "QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = ConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }


    public void initCredentials() {
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());

            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public QbConfigs getQbConfigs() {
        return qbConfigs;
    }

    protected String getQbConfigFileName() {
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }

    private void initQBSessionManager() {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession qbSession) {
                Log.d(TAG, "Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                Log.d(TAG, "Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                Log.d(TAG, "Session Deleted");
            }

            @Override
            public void onSessionRestored(QBSession qbSession) {
                Log.d(TAG, "Session Restored");
            }

            @Override
            public void onSessionExpired() {
                Log.d(TAG, "Session Expired");
            }

            @Override
            public void onProviderSessionExpired(String provider) {
                Log.d(TAG, "Session Expired for provider:" + provider);
            }
        });
    }

}
