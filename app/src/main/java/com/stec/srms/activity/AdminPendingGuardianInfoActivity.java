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
import com.stec.srms.model.PendingGuardian;
import com.stec.srms.util.EmailHandler;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

public class AdminPendingGuardianInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_guardian_info);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);
        int accountId = adminDBHandler.getAccountType("pendingGuardian").accountId;
        int newAccountId = adminDBHandler.getAccountType("guardian").accountId;
        Intent intent = getIntent();
        int pendingGuardianId = intent.getIntExtra("pendingGuardianId", -1);
        if (pendingGuardianId == -1) {
            finish();
            return;
        }
        PendingGuardian pendingGuardian = adminDBHandler.getPendingGuardian(pendingGuardianId);
        if (pendingGuardian == null) {
            finish();
            return;
        }

        ShapeableImageView guardianInfoProfileImage;
        TextView guardianInfoName, guardianInfoRelation, guardianInfoContact, guardianInfoEmail, guardianInfoStudentId;
        AppCompatButton guardianInfoDeleteButton, guardianInfoVerifyButton;

        guardianInfoProfileImage = findViewById(R.id.guardianInfoProfileImage);
        guardianInfoName = findViewById(R.id.guardianInfoName);
        guardianInfoRelation = findViewById(R.id.guardianInfoRelation);
        guardianInfoContact = findViewById(R.id.guardianInfoContact);
        guardianInfoEmail = findViewById(R.id.guardianInfoEmail);
        guardianInfoStudentId = findViewById(R.id.guardianInfoStudentId);
        guardianInfoDeleteButton = findViewById(R.id.guardianInfoDeleteButton);
        guardianInfoVerifyButton = findViewById(R.id.guardianInfoVerifyButton);

        Bitmap profileImage = Util.getImageFromInternalStorage(this, String.valueOf(accountId) + pendingGuardian.userId + ".jpeg");
        if (profileImage != null) guardianInfoProfileImage.setImageBitmap(profileImage);
        else guardianInfoProfileImage.setImageResource(R.drawable.default_profile);

        guardianInfoName.setText(pendingGuardian.name);
        guardianInfoRelation.setText(pendingGuardian.relation);
        guardianInfoContact.setText(pendingGuardian.contact);
        guardianInfoEmail.setText(pendingGuardian.email);
        guardianInfoStudentId.setText(String.valueOf(pendingGuardian.studentId));

        guardianInfoDeleteButton.setOnClickListener(v -> {
            boolean success = adminDBHandler.deletePendingGuardian(pendingGuardianId);
            if (!success) {
                Toast.databaseError(this, "Failed to delete guardian record");
                return;
            }
            success = Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingGuardian.userId + ".jpeg");
            if (!success) {
                Toast.generalError(this, "Failed to delete guardian profile image");
                return;
            }
            finish();
        });
        guardianInfoVerifyButton.setOnClickListener(v -> {
            if (!Util.isInternetConnected(this)) {
                Toast.generalError(this, "Internet connection is required");
                return;
            }

            int newGuardianId = adminDBHandler.movePendingGuardianToGuardianTable(pendingGuardian);
            if (newGuardianId == -1) {
                Toast.databaseError(this, "Failed to add guardian record");
                return;
            }
            adminDBHandler.deletePendingGuardian(pendingGuardianId);
            EmailHandler.sendNewUserIdMail(this, pendingGuardian.email, pendingGuardian.name, newGuardianId, success -> {
                if (!success) {
                    Toast.generalError(this, "Failed to send user id");
                    Toast.generalError(this, "Manually send user id (" + newGuardianId + ") to " + pendingGuardian.email);
                }
                finish();
            });

            int returnCode = Util.renameImageFromInternalStorage(this, String.valueOf(accountId) + pendingGuardian.userId + ".jpeg", String.valueOf(newAccountId) + newGuardianId + ".jpeg");
            if (returnCode == 1) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingGuardian.userId + ".jpeg");
                Toast.generalWarning(this, "Profile image already exists");
            } else if (returnCode == -1) {
                Toast.generalWarning(this, "Profile image not found");
            } else if (returnCode == -2) {
                Util.deleteImageFromInternalStorage(this, String.valueOf(accountId) + pendingGuardian.userId + ".jpeg");
                Toast.generalWarning(this, "Failed to rename profile image");
            }
        });
    }
}