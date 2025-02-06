package com.stec.srms.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

public class EnvVariable {
    private static final Properties properties = new Properties();

    public static void loadEnvVariable(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            assetManager.open("env");
            InputStream inputStream = assetManager.open("env");
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            Log.e("Class:EnvVariable", "Error loading env file: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key, "");
    }
}
