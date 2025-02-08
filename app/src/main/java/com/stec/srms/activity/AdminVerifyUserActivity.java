package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;
import com.stec.srms.database.AdminDBHandler;
import com.stec.srms.util.SessionManager;

public class AdminVerifyUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_verify_user);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);
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

        AppCompatButton adminPendingStudentButton, adminPendingFacultyButton, adminPendingGuardianButton;

        adminPendingStudentButton = findViewById(R.id.adminPendingStudentButton);
        adminPendingFacultyButton = findViewById(R.id.adminPendingFacultyButton);
        adminPendingGuardianButton = findViewById(R.id.adminPendingGuardianButton);

        adminPendingStudentButton.setOnClickListener(v -> startActivity(new Intent(this, AdminPendingStudentActivity.class)));
        adminPendingFacultyButton.setOnClickListener(v -> startActivity(new Intent(this, AdminPendingFacultyActivity.class)));
        adminPendingGuardianButton.setOnClickListener(v -> startActivity(new Intent(this, AdminPendingGuardianActivity.class)));
    }
}