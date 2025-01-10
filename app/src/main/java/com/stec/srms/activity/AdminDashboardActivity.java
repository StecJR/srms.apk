package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.stec.srms.R;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        AppCompatButton adminDashboardDBManagerButton;
        adminDashboardDBManagerButton = findViewById(R.id.adminDashboardDBManagerButton);

        adminDashboardDBManagerButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminDatabaseManagerActivity.class));
        });
    }
}