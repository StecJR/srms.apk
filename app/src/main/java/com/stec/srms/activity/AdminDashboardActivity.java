package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;
import com.stec.srms.util.SessionManager;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else if (!accountType.equals("admin")) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "faculty":
                    startActivity(new Intent(this, FacultyInfoActivity.class));
                    break;
                case "guardian":
                    startActivity(new Intent(this, GuardianInfoActivity.class));
                    break;
                default:
                    startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
            return;
        }

        AppCompatButton adminLogoutButton, adminVerifyUserButton;

        adminLogoutButton = findViewById(R.id.adminLogoutButton);
        adminVerifyUserButton = findViewById(R.id.adminVerifyUserButton);

        adminLogoutButton.setOnClickListener(v -> {
            sessionManager.deleteAdminSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        adminVerifyUserButton.setOnClickListener(v -> startActivity(new Intent(this, AdminVerifyUserActivity.class)));
    }
}