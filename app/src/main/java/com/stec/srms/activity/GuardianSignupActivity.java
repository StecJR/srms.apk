package com.stec.srms.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;
import com.stec.srms.R;
import com.stec.srms.adapter.DeptSelectorAdapter;
import com.stec.srms.database.GuardianDBHandler;
import com.stec.srms.fragment.OtpVerification;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.PendingGuardian;
import com.stec.srms.util.EmailHandler;
import com.stec.srms.util.PermissionHandler;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

import java.util.ArrayList;

public class GuardianSignupActivity extends AppCompatActivity {
    private String name, relation, contact, email, password1, password2;
    private int deptId, studentId;
    private ShapeableImageView guardianSignupImagePreviewer;
    private AppCompatButton guardianSignupImageSelectorButton;
    private Uri imageUri;
    private GuardianDBHandler database;

    private void showOtpBottomSheet(int otp) {
        OtpVerification otpVerification = new OtpVerification(String.valueOf(otp), () -> {
            int userId = database.addPendingGuardian(this, new PendingGuardian(-1, name, relation, contact, email, studentId, deptId, password1));
            if (userId != -1) {
                Util.saveImageToInternalStorage(this, imageUri, String.valueOf(database.getAccountType("pendingGuardian").accountId) + userId + ".jpeg");
                Toast.generalSuccess(this, "Signup complete. Wait for admin to verify");
                finish();
            }
        });
        otpVerification.show(getSupportFragmentManager(), "OtpBottomSheet");
    }

    private void verifyUser(String email) {
        int otp = Util.getOTP();
        EmailHandler.sendSignupOTPMail(this, email, name, otp, success -> {
            if (!success) {
                Toast.authenticationError(this, "Failed to send OTP");
                return;
            }
            showOtpBottomSheet(otp);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardian_signup);
        database = GuardianDBHandler.getInstance(this);
        EditText guardianSignupNameInput, guardianSignupRelationInput, guardianSignupContactInput, guardianSignupEmailInput,
                guardianSignupStudentIdInput, guardianSignupPwInput, guardianSignupRetypePwInput;
        Spinner guardianSignupDeptSpinner;
        AppCompatButton guardianSignupSignupButton;

        guardianSignupNameInput = findViewById(R.id.guardianSignupNameInput);
        guardianSignupRelationInput = findViewById(R.id.guardianSignupRelationInput);
        guardianSignupContactInput = findViewById(R.id.guardianSignupContactInput);
        guardianSignupEmailInput = findViewById(R.id.guardianSignupEmailInput);
        guardianSignupDeptSpinner = findViewById(R.id.guardianSignupDeptSpinner);
        guardianSignupStudentIdInput = findViewById(R.id.guardianSignupStudentIdInput);
        guardianSignupPwInput = findViewById(R.id.guardianSignupPwInput);
        guardianSignupRetypePwInput = findViewById(R.id.guardianSignupRetypePwInput);
        guardianSignupImageSelectorButton = findViewById(R.id.guardianSignupImageSelectorButton);
        guardianSignupImagePreviewer = findViewById(R.id.guardianSignupImagePreviewer);
        guardianSignupSignupButton = findViewById(R.id.guardianSignupSignupButton);

        ArrayList<DeptInfo> departments = new ArrayList<>(database.getDepartments());
        if (departments.get(0).deptId != -1) departments.add(0, new DeptInfo(-1, "Department", ""));
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, departments);
        guardianSignupDeptSpinner.setAdapter(deptSelectorAdapter);
        guardianSignupDeptSpinner.setSelection(0);

        Util.togglePasswordVisibility(this, guardianSignupPwInput);
        Util.togglePasswordVisibility(this, guardianSignupRetypePwInput);
        guardianSignupImagePreviewer.setVisibility(View.GONE);

        guardianSignupImageSelectorButton.setOnClickListener(v -> {
            if (!PermissionHandler.checkReadStoragePermission(this)) {
                PermissionHandler.requestReadStoragePermission(this);
                if (!PermissionHandler.checkReadStoragePermission(this)) return;
            }
            Util.openImagePicker(this);
        });
        guardianSignupImagePreviewer.setOnClickListener(v -> Util.openImagePicker(this));
        guardianSignupSignupButton.setOnClickListener(v -> {
            name = guardianSignupNameInput.getText().toString().trim();
            relation = guardianSignupRelationInput.getText().toString().trim();
            deptId = (int) guardianSignupDeptSpinner.getSelectedItemId();
            contact = guardianSignupContactInput.getText().toString().trim();
            email = guardianSignupEmailInput.getText().toString().trim();
            password1 = guardianSignupPwInput.getText().toString().trim();
            password2 = guardianSignupRetypePwInput.getText().toString().trim();

            if (name.isBlank()) {
                Toast.generalError(this, "Name field is empty");
                return;
            }
            if (relation.isBlank()) {
                Toast.generalError(this, "Relation field is empty");
                return;
            }
            if (contact.isBlank()) {
                Toast.generalError(this, "Contact field is empty");
                return;
            }
            if (!Util.validPhoneNumber(contact)) {
                Toast.generalError(this, "Invalid contact number");
                return;
            }
            if (email.isBlank()) {
                Toast.generalError(this, "Email field is empty");
                return;
            }
            if (!Util.validEmail(email)) {
                Toast.generalError(this, "Invalid email address");
                return;
            }
            if (database.isEmailExists(email)) {
                Toast.generalError(this, "Email already exists");
                return;
            }
            if (deptId == -1) {
                Toast.generalError(this, "Select department");
                return;
            }
            try {
                studentId = Integer.parseInt(guardianSignupStudentIdInput.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.generalError(this, "Invalid student ID");
                return;
            }
            if (!database.isStudentExists(deptId, studentId)) {
                Toast.generalError(this, "Student ID does not exist");
                return;
            }
            if (password1.isBlank()) {
                Toast.generalError(this, "Password field is empty");
                return;
            }
            if (password2.isBlank()) {
                Toast.generalError(this, "Retype password");
                return;
            }
            if (!password1.equals(password2)) {
                Toast.generalError(this, "Password mismatch");
                return;
            }
            if (imageUri == null) {
                Toast.generalError(this, "Select profile image");
                return;
            }
            if (!Util.isInternetConnected(this)) {
                Toast.generalError(this, "No internet connection");
                return;
            }

            verifyUser(email);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionHandler.REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            String mimeType = getContentResolver().getType(imageUri);
            if (mimeType == null || (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png"))) {
                Toast.generalError(this, "Please select a JPG, JPEG, or PNG image.");
                return;
            }
            try {
                Bitmap imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                if (imageBitmap.getWidth() != 256 || imageBitmap.getHeight() != 256)
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 256, 256, true);
                guardianSignupImageSelectorButton.setVisibility(View.GONE);
                guardianSignupImagePreviewer.setVisibility(View.VISIBLE);
                guardianSignupImagePreviewer.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                Toast.generalError(this, "Failed to load image");
            }
        }
    }
}