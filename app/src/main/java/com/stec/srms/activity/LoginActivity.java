package com.stec.srms.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        SessionManager sessionManager = SessionManager.getInstance(this);
        sessionManager.deleteAllSessions();

        BottomNavigationView loginBottomNav = findViewById(R.id.loginBottomNav);
        loginBottomNav.setItemIconTintList(null);
        initializeLoginFragments();
        loadFragment(loginFragments.get(R.id.loginBottomNavStudent));
        loginBottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = loginFragments.get(item.getItemId());
            if (selectedFragment == null) {
                return false;
            }
            loadFragment(selectedFragment);
            return true;
        });
    }

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
}