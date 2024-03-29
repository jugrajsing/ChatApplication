package com.example.hp.chatapplication.VideoCall.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.hp.chatapplication.VideoCall.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigParser {

    private Context context;

    public ConfigParser() {
        context = App.getInstance().getApplicationContext();
    }

    public static String getJsonAsString(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        StringBuilder buf = new StringBuilder();
        InputStream json = manager.open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str = in.readLine()) != null) {
            buf.append(str);
        }

        in.close();

        return buf.toString();
    }

    public String getConfigsAsJsonString(String fileName) throws IOException {
        return getJsonAsString(fileName, context);
    }
}
