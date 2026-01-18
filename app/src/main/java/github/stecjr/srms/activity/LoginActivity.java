package github.stecjr.srms.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import github.stecjr.srms.R;
import github.stecjr.srms.fragment.LoginAdminFragment;
import github.stecjr.srms.fragment.LoginFacultyFragment;
import github.stecjr.srms.fragment.LoginGuardianFragment;
import github.stecjr.srms.fragment.LoginStudentFragment;
import github.stecjr.srms.util.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private final Map<Integer, Fragment> loginFragments = new HashMap<>();

    private void initializeLoginFragments() {
        loginFragments.put(R.id.loginBottomNavStudent, new LoginStudentFragment());
        loginFragments.put(R.id.loginBottomNavFaculty, new LoginFacultyFragment());
        loginFragments.put(R.id.loginBottomNavGuardian, new LoginGuardianFragment());
        loginFragments.put(R.id.loginBottomNavAdmin, new LoginAdminFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.loginFrame, fragment).commit();
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