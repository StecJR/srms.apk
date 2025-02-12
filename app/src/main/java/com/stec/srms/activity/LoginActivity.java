package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stec.srms.R;
import com.stec.srms.fragment.LoginAdminFragment;
import com.stec.srms.fragment.LoginFacultyFragment;
import com.stec.srms.fragment.LoginGuardianFragment;
import com.stec.srms.fragment.LoginStudentFragment;
import com.stec.srms.util.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private final Map<Integer, Fragment> loginFragments = new HashMap<>();

    private void initializeLoginFragments() {
        loginFragments.put(R.id.loginBottomNavStudent, new LoginStudentFragment());
        loginFragments.put(R.id.loginBottomNavFaculty, new LoginFacultyFragment());
        loginFragments.put(R.id.loginBottomNavGuardian, new LoginGuardianFragment());
        loginFragments.put(R.id.loginBottomNavAdmin, new LoginAdminFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.loginFrame, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        SessionManager sessionManager = SessionManager.getInstance(this);
        sessionManager.deleteAllSessions();
        Intent intent = getIntent();
        int activeMenuIndex = intent.getIntExtra("activeMenuIndex", 0);

        BottomNavigationView loginBottomNav = findViewById(R.id.loginBottomNav);
        loginBottomNav.setItemIconTintList(null);
        initializeLoginFragments();
        switch (activeMenuIndex) {
            case 0:
                loadFragment(loginFragments.get(R.id.loginBottomNavStudent));
                loginBottomNav.getMenu().getItem(0).setChecked(true);
                break;
            case 1:
                loadFragment(loginFragments.get(R.id.loginBottomNavFaculty));
                loginBottomNav.getMenu().getItem(1).setChecked(true);
                break;
            case 2:
                loadFragment(loginFragments.get(R.id.loginBottomNavGuardian));
                loginBottomNav.getMenu().getItem(2).setChecked(true);
                break;
            default:
                loadFragment(loginFragments.get(R.id.loginBottomNavAdmin));
                loginBottomNav.getMenu().getItem(3).setChecked(true);
                break;
        }

        loginBottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = loginFragments.get(item.getItemId());
            if (selectedFragment == null) return false;
            loadFragment(selectedFragment);
            return true;
        });
    }
}