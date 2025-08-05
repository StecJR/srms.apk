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
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 3);
            startActivity(intent);
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
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("activeMenuIndex", 3);
                    startActivity(intent);
            }
            finish();
            return;
        }

        AppCompatButton adminLogoutButton, adminNoticeBoardButton, adminVerifyUserButton;

        adminLogoutButton = findViewById(R.id.adminLogoutButton);
        adminNoticeBoardButton = findViewById(R.id.adminNoticeBoardButton);
        adminVerifyUserButton = findViewById(R.id.adminVerifyUserButton);

        adminLogoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 3);
            startActivity(intent);
            finish();
        });
        adminNoticeBoardButton.setOnClickListener(v -> startActivity(new Intent(this, UserNoticeBoardActivity.class)));
        adminVerifyUserButton.setOnClickListener(v -> startActivity(new Intent(this, AdminVerifyUserActivity.class)));
    }
}