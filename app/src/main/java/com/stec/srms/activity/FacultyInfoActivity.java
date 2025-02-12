package com.stec.srms.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;
import com.stec.srms.R;
import com.stec.srms.database.FacultyDBHandler;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.FacultyInfo;
import com.stec.srms.model.FacultySession;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Util;

public class FacultyInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_info);
        FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 1);
            startActivity(intent);
            finish();
            return;
        } else if (!accountType.equals("faculty")) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "guardian":
                    startActivity(new Intent(this, GuardianInfoActivity.class));
                    break;
                case "admin":
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                    break;
                default:
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("activeMenuIndex", 1);
                    startActivity(intent);
            }
            finish();
            return;
        }

        // Handle invalid student session
        FacultySession facultySession = sessionManager.getFacultySession();
        if (facultySession == null || !facultyDBHandler.isValidFaculty(facultySession.facultyId)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 1);
            startActivity(intent);
            finish();
            return;
        }

        ShapeableImageView facultyInfoProfileImage;
        TextView facultyInfoFacultyId, facultyInfoName, facultyInfoGender, facultyInfoDept, facultyInfoContact, facultyInfoEmail;
        AppCompatButton facultyInfoResultButton, facultyInfoLogoutButton;
        FacultyInfo facultyInfo = facultyDBHandler.getFacultyInfo(facultySession.facultyId);
        DeptInfo deptInfo = facultyDBHandler.getDepartment(facultyInfo.deptId);

        facultyInfoProfileImage = findViewById(R.id.facultyInfoProfileImage);
        facultyInfoFacultyId = findViewById(R.id.facultyInfoFacultyId);
        facultyInfoName = findViewById(R.id.facultyInfoName);
        facultyInfoGender = findViewById(R.id.facultyInfoGender);
        facultyInfoDept = findViewById(R.id.facultyInfoDept);
        facultyInfoContact = findViewById(R.id.facultyInfoContact);
        facultyInfoEmail = findViewById(R.id.facultyInfoEmail);
        facultyInfoResultButton = findViewById(R.id.facultyInfoResultButton);
        facultyInfoLogoutButton = findViewById(R.id.facultyInfoLogoutButton);

        int accountId = facultyDBHandler.getAccountType("faculty").accountId;
        Bitmap profileImage = Util.getImageFromInternalStorage(this, String.valueOf(accountId) + facultyInfo.facultyId + ".jpeg");
        if (profileImage != null) facultyInfoProfileImage.setImageBitmap(profileImage);
        else facultyInfoProfileImage.setImageResource(R.drawable.default_profile);

        facultyInfoFacultyId.setText(String.valueOf(facultyInfo.facultyId));
        facultyInfoName.setText(facultyInfo.name);
        facultyInfoGender.setText(facultyInfo.gender);
        facultyInfoDept.setText(deptInfo.longDesc);
        facultyInfoContact.setText(facultyInfo.contact);
        facultyInfoEmail.setText(facultyInfo.email);

        facultyInfoResultButton.setOnClickListener(v -> startActivity(new Intent(this, FacultyResultActivity.class)));
        facultyInfoLogoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 1);
            startActivity(intent);
            finish();
        });
    }
}