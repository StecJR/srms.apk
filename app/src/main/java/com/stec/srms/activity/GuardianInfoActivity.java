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
import com.stec.srms.database.StudentDBHandler;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.model.GuardianInfo;
import com.stec.srms.model.GuardianSession;
import com.stec.srms.model.StudentInfo;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Util;

public class GuardianInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardian_info);
        StudentDBHandler guardianDBHandler = StudentDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else if (!accountType.equals("guardian")) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "faculty":
                    startActivity(new Intent(this, FacultyInfoActivity.class));
                    break;
                case "admin":
                    startActivity(new Intent(this, AdminInfo.class));
                    break;
                default:
                    startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
            return;
        }

        // Handle invalid guardian session
        GuardianSession guardianSession = sessionManager.getGuardianSession();
        if (guardianSession == null ||
                !guardianDBHandler.isValidGuardian(guardianSession.guardianId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ShapeableImageView guardianInfoProfileImage;
        TextView guardianInfoStudentName, guardianInfoStudentDept, guardianInfoName,
                guardianInfoRelation, guardianInfoContact, guardianInfoEmail;
        AppCompatButton guardianInfoLogoutButton, guardianStudentInfoButton, guardianStudentResultButton;
        StudentInfo studentInfo = guardianDBHandler.getStudentinfo(guardianSession.deptId, guardianSession.studentId);
        GuardianInfo guardianInfo = guardianDBHandler.getGuardianinfo(guardianSession.guardianId);

        guardianInfoProfileImage = findViewById(R.id.guardianInfoProfileImage);
        guardianInfoStudentName = findViewById(R.id.guardianInfoStudentName);
        guardianInfoStudentDept = findViewById(R.id.guardianInfoStudentDept);
        guardianInfoName = findViewById(R.id.guardianInfoName);
        guardianInfoRelation = findViewById(R.id.guardianInfoRelation);
        guardianInfoContact = findViewById(R.id.guardianInfoContact);
        guardianInfoEmail = findViewById(R.id.guardianInfoEmail);
        guardianInfoLogoutButton = findViewById(R.id.guardianInfoLogoutButton);
        guardianStudentInfoButton = findViewById(R.id.GuardianStudentInfoButton);
        guardianStudentResultButton = findViewById(R.id.GuardianStudentResultButton);

        int accountId = guardianDBHandler.getAccountType("guardian").accountId;
        Bitmap profileImage = Util.getImageFromInternalStorage(
                this,
                String.valueOf(accountId) + studentInfo.studentId + ".jpeg");
        if (profileImage != null) guardianInfoProfileImage.setImageBitmap(profileImage);
        else guardianInfoProfileImage.setImageResource(R.drawable.default_profile);

        guardianInfoStudentName.setText(studentInfo.name);
        guardianInfoStudentDept.setText(guardianDBHandler.getDepartment(studentInfo.deptId).longDesc);

        guardianInfoName.setText(guardianInfo.name);
        guardianInfoRelation.setText(guardianInfo.relation);
        guardianInfoContact.setText(guardianInfo.contact);
        guardianInfoEmail.setText(guardianInfo.email);

        guardianInfoLogoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        guardianStudentInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentInfoActivity.class);
            intent.putExtra("hideTopButtons", true);
            startActivity(intent);
        });
        guardianStudentResultButton.setOnClickListener(v -> startActivity(new Intent(this, StudentResultActivity.class)));
    }
}