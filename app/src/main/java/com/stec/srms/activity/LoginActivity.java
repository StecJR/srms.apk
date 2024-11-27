package com.stec.srms.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stec.srms.R;
import com.stec.srms.fragment.LoginAdminFragment;
import com.stec.srms.fragment.LoginGuardianFragment;
import com.stec.srms.fragment.LoginStudentFragment;
import com.stec.srms.fragment.LoginTeacherFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        FrameLayout loginFrame = findViewById(R.id.loginFrame);
        BottomNavigationView loginBottomNav = findViewById(R.id.loginBottomNav);
        loginBottomNav.setItemIconTintList(null);

        LoginStudentFragment loginStudentFragment = new LoginStudentFragment();
        LoginTeacherFragment loginTeacherFragment = new LoginTeacherFragment();
        LoginGuardianFragment loginGuardianFragment = new LoginGuardianFragment();
        LoginAdminFragment loginAdminFragment = new LoginAdminFragment();
        loadFragment(loginStudentFragment);

        loginBottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.loginNavStudent) {
                loadFragment(loginStudentFragment);
            }
            else if (item.getItemId() == R.id.loginNavTeacher) {
                loadFragment(loginTeacherFragment);
            }
            else if (item.getItemId() == R.id.loginNavGuardian) {
                loadFragment(loginGuardianFragment);
            }
            else if (item.getItemId() == R.id.loginNavAdmin) {
                loadFragment(loginAdminFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.loginFrame, fragment)
                .commit();
    }
}