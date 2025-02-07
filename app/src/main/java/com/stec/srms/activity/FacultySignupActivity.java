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
import com.stec.srms.adapter.SessionSelectorAdapter;
import com.stec.srms.database.FacultyDBHandler;
import com.stec.srms.fragment.OtpVerification;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.PendingFaculty;
import com.stec.srms.model.SessionInfo;
import com.stec.srms.util.EmailHandler;
import com.stec.srms.util.PermissionHandler;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

import java.util.ArrayList;

public class FacultySignupActivity extends AppCompatActivity {
    private String name, contact, email, address, password1, password2;
    private int genderId, deptId;
    private ShapeableImageView facultySignupImagePreviewer;
    private AppCompatButton facultySignupImageSelectorButton;
    private Uri imageUri;
    private FacultyDBHandler database;

    private void showOtpBottomSheet(int otp) {
        OtpVerification otpVerification = new OtpVerification(String.valueOf(otp), () -> {
            String gender = genderId == 1 ? "Male" : "Female";
            int userId = database.addPendingFaculty(new PendingFaculty(-1, name, gender, deptId, contact, email, address, password1));
            if (userId != -1) {
                Util.saveImageToInternalStorage(
                        this,
                        imageUri,
                        String.valueOf(database.getAccountType("pendingFaculty").accountId) + userId + ".jpeg"
                );
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
        setContentView(R.layout.activity_faculty_signup);
        database = FacultyDBHandler.getInstance(this);
        EditText facultySignupNameInput, facultySignupContactInput, facultySignupEmailInput,
                facultySignupAddressInput, facultySignupPwInput, facultySignupRetypePwInput;
        Spinner facultySignupGenderSpinner, facultySignupDeptSpinner;
        AppCompatButton facultySignupSignupButton;

        facultySignupNameInput = findViewById(R.id.facultySignupNameInput);
        facultySignupGenderSpinner = findViewById(R.id.facultySignupGenderSpinner);
        facultySignupDeptSpinner = findViewById(R.id.facultySignupDeptSpinner);
        facultySignupContactInput = findViewById(R.id.facultySignupContactInput);
        facultySignupEmailInput = findViewById(R.id.facultySignupEmailInput);
        facultySignupAddressInput = findViewById(R.id.facultySignupAddressInput);
        facultySignupPwInput = findViewById(R.id.facultySignupPwInput);
        facultySignupRetypePwInput = findViewById(R.id.facultySignupRetypePwInput);
        facultySignupImageSelectorButton = findViewById(R.id.facultySignupImageSelectorButton);
        facultySignupImagePreviewer = findViewById(R.id.facultySignupImagePreviewer);
        facultySignupSignupButton = findViewById(R.id.facultySignupSignupButton);

        ArrayList<SessionInfo> gender = new ArrayList<>();
        gender.add(new SessionInfo(-1, "Gender"));
        gender.add(new SessionInfo(1, "Male"));
        gender.add(new SessionInfo(2, "Female"));
        SessionSelectorAdapter genderSelectorAdapter = new SessionSelectorAdapter(this, gender);
        facultySignupGenderSpinner.setAdapter(genderSelectorAdapter);
        facultySignupGenderSpinner.setSelection(0);

        ArrayList<DeptInfo> departments = new ArrayList<>(database.getDepartments());
        if (departments.get(0).deptId != -1) departments.add(0, new DeptInfo(-1, "Department", ""));
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, departments);
        facultySignupDeptSpinner.setAdapter(deptSelectorAdapter);
        facultySignupDeptSpinner.setSelection(0);

        Util.togglePasswordVisibility(this, facultySignupPwInput);
        Util.togglePasswordVisibility(this, facultySignupRetypePwInput);
        facultySignupImagePreviewer.setVisibility(View.GONE);

        facultySignupImageSelectorButton.setOnClickListener(v -> {
            if (!PermissionHandler.checkReadStoragePermission(this)) {
                PermissionHandler.requestReadStoragePermission(this);
                if (!PermissionHandler.checkReadStoragePermission(this)) {
                    Toast.generalError(this, "Storage permission is required to select image");
                    return;
                }
            }
            Util.openImagePicker(this);
        });
        facultySignupImagePreviewer.setOnClickListener(v -> Util.openImagePicker(this));
        facultySignupSignupButton.setOnClickListener(v -> {
            name = facultySignupNameInput.getText().toString().trim();
            genderId = (int) facultySignupGenderSpinner.getSelectedItemId();
            deptId = (int) facultySignupDeptSpinner.getSelectedItemId();
            contact = facultySignupContactInput.getText().toString().trim();
            email = facultySignupEmailInput.getText().toString().trim();
            address = facultySignupAddressInput.getText().toString().trim();
            password1 = facultySignupPwInput.getText().toString().trim();
            password2 = facultySignupRetypePwInput.getText().toString().trim();

            if (name.isBlank()) {
                Toast.generalError(this, "Name field is empty");
                return;
            }
            if (genderId == -1) {
                Toast.generalError(this, "Select gender");
                return;
            }
            if (deptId == -1) {
                Toast.generalError(this, "Select department");
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
            if (address.isBlank()) {
                Toast.generalError(this, "Address field is empty");
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
                Bitmap imageBitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(getContentResolver(), imageUri)
                );
                if (imageBitmap.getWidth() != 256 || imageBitmap.getHeight() != 256)
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 256, 256, true);
                facultySignupImageSelectorButton.setVisibility(View.GONE);
                facultySignupImagePreviewer.setVisibility(View.VISIBLE);
                facultySignupImagePreviewer.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                Toast.generalError(this, "Failed to load image");
            }
        }
    }
}