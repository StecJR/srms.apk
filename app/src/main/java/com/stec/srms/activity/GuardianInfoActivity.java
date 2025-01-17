package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;

public class GuardianInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardian_info);

        AppCompatButton GuardianStudentInfoButton, GuardianStudentResultButton;

        GuardianStudentInfoButton = findViewById(R.id.GuardianStudentInfoButton);
        GuardianStudentResultButton = findViewById(R.id.GuardianStudentResultButton);

        GuardianStudentInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentInfoActivity.class);
            intent.putExtra("hideTopButtons", true);
            startActivity(intent);
        });
        GuardianStudentResultButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentResultActivity.class));
        });
    }
}