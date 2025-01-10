package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;

public class StudentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_info);

        Intent intent = getIntent();
        boolean hideTopButtons = intent.getBooleanExtra("hideTopButtons", false);
        AppCompatButton studentInfoResultButton, studentInfoLogoutButton;

        studentInfoResultButton = findViewById(R.id.studentInfoResultButton);
        studentInfoLogoutButton = findViewById(R.id.guardianInfoLogoutButton);

        if (hideTopButtons) {
            studentInfoResultButton.setVisibility(View.INVISIBLE);
            studentInfoLogoutButton.setVisibility(View.INVISIBLE);
        }

        studentInfoResultButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentResultActivity.class));
        });
    }
}