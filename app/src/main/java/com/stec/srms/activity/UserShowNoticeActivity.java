package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;

public class UserShowNoticeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_show_notice);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String createdAt = intent.getStringExtra("createdAt");

        TextView noticeTitle, noticeDescription, noticeCreatedAt;
        noticeTitle = findViewById(R.id.noticeTitle);
        noticeDescription = findViewById(R.id.noticeDescription);
        noticeCreatedAt = findViewById(R.id.noticeCreatedAt);
        noticeTitle.setText(title);
        noticeDescription.setText(description);
        noticeCreatedAt.setText(createdAt);
    }
}