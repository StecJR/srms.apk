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
import com.stec.srms.model.PendingFaculty;
import com.stec.srms.util.EmailHandler;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

public class AdminPendingFacultyInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_faculty_info);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);
        int accountId = adminDBHandler.getAccountType("pendingFaculty").accountId;
        int newAccountId = adminDBHandler.getAccountType("faculty").accountId;
        Intent intent = getIntent();
        int pendingFacultyId = intent.getIntExtra("pendingFacultyId", -1);
        if (pendingFacultyId == -1) {
            finish();
            return;
        }
        PendingFaculty pendingFaculty = adminDBHandler.getPendingFaculty(pendingFacultyId);
        if (pendingFaculty == null) {
            finish();
            return;
        }

        ShapeableImageView facultyInfoProfileImage;
        TextView facultyInfoName, facultyInfoGender, facultyInfoDept, facultyInfoContact, facultyInfoEmail;
        AppCompatButton facultyInfoDeleteButton, facultyInfoVerifyButton;

        facultyInfoProfileImage = findViewById(R.id.facultyInfoProfileImage);
        facultyInfoName = findViewById(R.id.facultyInfoName);
        facultyInfoGender = findViewById(R.id.facultyInfoGender);
        facultyInfoDept = findViewById(R.id.facultyInfoDept);
        facultyInfoContact = findViewById(R.id.facultyInfoContact);
        facultyInfoEmail = findViewById(R.id.facultyInfoEmail);
        facultyInfoDeleteButton = findViewById(R.id.facultyInfoDeleteButton);
        facultyInfoVerifyButton = findViewById(R.id.facultyInfoVerifyButton);

        Bitmap profileImage = Util.getImageFromInternalStorage(this, String.valueOf(accountId) + pendingFaculty.userId + ".jpeg");
        if (profileImage != null) facultyInfoProfileImage.setImageBitmap(profileImage);
        else facultyInfoProfileImage.setImageResource(R.drawable.default_profile);

        facultyInfoName.setText(pendingFaculty.name);
        facultyInfoGender.setText(pendingFaculty.gender);
        facultyInfoDept.setText(adminDBHandler.getDepartment(pendingFaculty.deptId).longDesc);
        facultyInfoContact.setText(pendingFaculty.contact);
        facultyInfoEmail.setText(pendingFaculty.email);

        facultyInfoDeleteButton.setOnClickListener(v -> {
            boolean success = adminDBHandler.deletePendingFaculty(pendingFacultyId);
            if (!success) {
                Toast.databaseError(this, "Failed to delete faculty record");
                return;
            }
            success = Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingFaculty.userId + ".jpeg");
            if (!success) {
                Toast.generalError(this, "Failed to delete faculty profile image");
                return;
            }
            finish();
        });
        facultyInfoVerifyButton.setOnClickListener(v -> {
            if (!Util.isInternetConnected(this)) {
                Toast.generalError(this, "Internet connection is required");
                return;
            }

            int newFacultyId = adminDBHandler.movePendingFacultyToFacultyTable(pendingFaculty);
            if (newFacultyId == -1) {
                Toast.databaseError(this, "Failed to add student record");
                return;
            }
            adminDBHandler.deletePendingFaculty(pendingFacultyId);
            EmailHandler.sendNewUserIdMail(this, pendingFaculty.email, pendingFaculty.name, newFacultyId, success -> {
                if (!success) {
                    Toast.generalError(this, "Failed to send user id");
                    Toast.generalError(this, "Manually send user id (" + newFacultyId + ") to " + pendingFaculty.email);
                }
                finish();
            });

            int returnCode = Util.renameImageFromInternalStorage(this, String.valueOf(accountId) + pendingFaculty.userId + ".jpeg", String.valueOf(newAccountId) + newFacultyId + ".jpeg");
            if (returnCode == 1) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingFaculty.userId + ".jpeg");
                Toast.generalWarning(this, "Profile image already exists");
            } else if (returnCode == -1) {
                Toast.generalWarning(this, "Profile image not found");
            } else if (returnCode == -2) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingFaculty.userId + ".jpeg");
                Toast.generalWarning(this, "Failed to rename profile image");
            }
        });
    }
}