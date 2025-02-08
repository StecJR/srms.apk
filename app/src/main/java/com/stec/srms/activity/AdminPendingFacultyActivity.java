package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;

public class AdminPendingFacultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_faculty);

        TableLayout adminPendingFacultyTable;

        adminPendingFacultyTable = findViewById(R.id.adminPendingFacultyTable);
        adminPendingFacultyTable.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminPendingFacultyInfoActivity.class));
        });
    }
}