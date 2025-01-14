package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.database.Database;
import com.stec.srms.util.SessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Database database = Database.getInstance(getApplicationContext());
        database.getWritableDatabase();
        sessionManager = SessionManager.getInstance(getApplicationContext());

        if (sessionManager.isFirstTime()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        } else if (!sessionManager.validSession().isEmpty()) {
            // Show the activity according to the user type
            // setContentView(R.layout.activity_main);
            Log.d("MainActivity", "User type is: " + sessionManager.getAccountType());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}