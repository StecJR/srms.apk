package com.stec.srms.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.util.Date;

public class SessionManager {
    private static SharedPreferences sharedPreference;
    private static SessionManager sessionManager;

    private SessionManager(Context context) {
        if (sharedPreference == null) {
            try {
                sharedPreference = EncryptedSharedPreferences.create(
                        context,
                        "encrypted_session_pref",
                        new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            } catch (Exception e) {
                Log.e("SessionCreationError", String.valueOf(e));
                e.printStackTrace();
            }
        }
    }
    public static synchronized SessionManager instance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    public void createSession(String userId, String userType, int expiryDay) {
        sharedPreference.edit()
                .putString("sessionUserId", userId)
                .putString("sessionUserType", userType)
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }
    public void deleteSession() {
        sharedPreference.edit()
                .remove("sessionUserId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }
    public Boolean isValidSession() {
        long expiryDate = sharedPreference.getLong("sessionExpiryDate", 0);
        if (expiryDate == 0 || expiryDate < new Date().getTime()) {
            deleteSession();
            return false;
        }
        return true;
    }

    public String getUserId() {
        return sharedPreference.getString("sessionUserId", "");
    }
    public String getUserType() {
        return sharedPreference.getString("sessionUserType", "");
    }

    public Boolean isFirstTime() {
        return sharedPreference.getBoolean("isFirstTime", true);
    }
    public void turnOffFirstTime() {
        sharedPreference.edit()
                .putBoolean("isFirstTime", false)
                .apply();
    }
}
