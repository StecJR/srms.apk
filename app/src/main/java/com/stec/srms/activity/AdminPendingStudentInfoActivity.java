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
import com.stec.srms.database.AdminDBHandler;
import com.stec.srms.model.PendingStudent;
import com.stec.srms.util.EmailHandler;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

public class AdminPendingStudentInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_student_info);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);
        int accountId = adminDBHandler.getAccountType("pendingStudent").accountId;
        int newAccountId = adminDBHandler.getAccountType("student").accountId;
        Intent intent = getIntent();
        int pendingStudentId = intent.getIntExtra("pendingStudentId", -1);
        if (pendingStudentId == -1) {
            finish();
            return;
        }
        PendingStudent pendingStudent = adminDBHandler.getPendingStudent(pendingStudentId);
        if (pendingStudent == null) {
            finish();
            return;
        }

        ShapeableImageView studentInfoProfileImage;
        TextView studentInfoName, studentInfoBirthDate, studentInfoGender, studentInfoDept,
                studentInfoSession, studentInfoContact, studentInfoEmail, studentInfoAddress;
        AppCompatButton studentInfoDeleteButton, studentInfoVerifyButton;

        studentInfoProfileImage = findViewById(R.id.studentInfoProfileImage);
        studentInfoName = findViewById(R.id.studentInfoName);
        studentInfoBirthDate = findViewById(R.id.studentInfoBirthDate);
        studentInfoGender = findViewById(R.id.studentInfoGender);
        studentInfoDept = findViewById(R.id.studentInfoDept);
        studentInfoSession = findViewById(R.id.studentInfoSession);
        studentInfoContact = findViewById(R.id.studentInfoContact);
        studentInfoEmail = findViewById(R.id.studentInfoEmail);
        studentInfoAddress = findViewById(R.id.studentInfoAddress);
        studentInfoDeleteButton = findViewById(R.id.studentInfoDeleteButton);
        studentInfoVerifyButton = findViewById(R.id.studentInfoVerifyButton);

        Bitmap profileImage = Util.getImageFromInternalStorage(
                this,
                String.valueOf(accountId) + pendingStudent.userId + ".jpeg"
        );
        if (profileImage != null) studentInfoProfileImage.setImageBitmap(profileImage);
        else studentInfoProfileImage.setImageResource(R.drawable.default_profile);

        studentInfoName.setText(pendingStudent.name);
        studentInfoBirthDate.setText(pendingStudent.birthDate);
        studentInfoGender.setText(pendingStudent.gender);
        studentInfoDept.setText(adminDBHandler.getDepartment(pendingStudent.deptId).longDesc);
        studentInfoSession.setText(adminDBHandler.getSession(pendingStudent.sessionId).desc);
        studentInfoContact.setText(pendingStudent.contact);
        studentInfoEmail.setText(pendingStudent.email);
        studentInfoAddress.setText(pendingStudent.address);

        studentInfoDeleteButton.setOnClickListener(v -> {
            boolean success = adminDBHandler.deletePendingStudent(pendingStudentId);
            if (!success) {
                Toast.databaseError(this, "Failed to delete student record");
                return;
            }
            success = Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingStudent.userId + ".jpeg");
            if (!success) {
                Toast.generalError(this, "Failed to delete student profile image");
                return;
            }
            finish();
        });
        studentInfoVerifyButton.setOnClickListener(v -> {
            if (!Util.isInternetConnected(this)) {
                Toast.generalError(this, "Internet connection is required");
                return;
            }

            int newStudentId = adminDBHandler.movePendingStudentToStudentTable(pendingStudent);
            if (newStudentId == -1) {
                Toast.databaseError(this, "Failed to add student record");
                return;
            }
            adminDBHandler.deletePendingStudent(pendingStudentId);
            EmailHandler.sendNewUserIdMail(this, pendingStudent.email, pendingStudent.name, newStudentId, success -> {
                if (!success) {
                    Toast.generalError(this, "Failed to send user id");
                    Toast.generalError(this, "Manually send user id (" + newStudentId + ") to " + pendingStudent.email);
                }
                finish();
            });

            int returnCode = Util.renameImageFromInternalStorage(
                    this,
                    String.valueOf(accountId) + pendingStudent.userId + ".jpeg",
                    String.valueOf(newAccountId) + newStudentId + ".jpeg"
            );
            if (returnCode == 1) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingStudent.userId + ".jpeg");
                Toast.generalWarning(this, "Profile image already exists");
            } else if (returnCode == -1) {
                Toast.generalWarning(this, "Profile image not found");
            } else if (returnCode == -2) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingStudent.userId + ".jpeg");
                Toast.generalWarning(this, "Failed to rename profile image");
            }
        });
    }
}