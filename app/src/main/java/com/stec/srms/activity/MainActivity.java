package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;
import com.stec.srms.util.SessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        sessionManager = SessionManager.instance(this);

        if (sessionManager.isFirstTime()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        } else if (sessionManager.isValidSession()) {
            // Show the activity according to the user type
            // setContentView(R.layout.activity_main);
            Log.d("MainActivity", "User type is: " + sessionManager.getUserType());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}