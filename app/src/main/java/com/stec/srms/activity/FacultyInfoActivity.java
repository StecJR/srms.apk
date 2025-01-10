package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;

public class FacultyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_info);

        AppCompatButton teacherInfoResultButton = findViewById(R.id.facultyInfoResultButton);
        teacherInfoResultButton.setOnClickListener(v -> {
            startActivity(new Intent(this, FacultyResultActivity.class));
        });

    }
}