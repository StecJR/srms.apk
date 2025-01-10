package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;
import com.stec.srms.util.SessionManager;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        AppCompatButton onboardingExitButton = findViewById(R.id.onboardingExitButton);
        onboardingExitButton.setOnClickListener(v -> {
            SessionManager.instance(this).turnOffFirstTime();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}