package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.database.Database;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.util.SessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Database database = Database.getInstance(getApplicationContext());
        database.getWritableDatabase();
        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
        String accountType = sessionManager.validSession();

        if (sessionManager.isFirstTime()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        } else if (accountType != null) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "guardian":
                    startActivity(new Intent(this, GuardianInfoActivity.class));
                    break;
                case "faculty":
                    startActivity(new Intent(this, FacultyInfoActivity.class));
                    break;
                case "admin":
                    startActivity(new Intent(this, AdminInfo.class));
                    break;
                default:
                    startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}