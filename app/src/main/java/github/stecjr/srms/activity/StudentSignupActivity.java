package github.stecjr.srms.activity;

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

import java.util.ArrayList;
import java.util.Calendar;

import github.stecjr.srms.R;
import github.stecjr.srms.adapter.DeptSelectorAdapter;
import github.stecjr.srms.adapter.SessionSelectorAdapter;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.fragment.OtpVerification;
import github.stecjr.srms.model.DeptInfo;
import github.stecjr.srms.model.PendingStudent;
import github.stecjr.srms.model.SessionInfo;
import github.stecjr.srms.util.EmailHandler;
import github.stecjr.srms.util.PermissionHandler;
import github.stecjr.srms.util.Toast;
import github.stecjr.srms.util.Util;

public class StudentSignupActivity extends AppCompatActivity {
    private String name, dob, contact, email, address, password1, password2;
    private int genderId, deptId, sessionId;
    private ShapeableImageView studentSignupImagePreviewer;
    private AppCompatButton studentSignupImageSelectorButton;
    private Uri imageUri;
    private StudentDBHandler database;

    private void showOtpBottomSheet(int otp) {
        OtpVerification otpVerification = new OtpVerification(String.valueOf(otp), () -> {
            String gender = genderId == 1 ? "Male" : "Female";
            int userId = database.addPendingStudent(this, new PendingStudent(-1, name, dob, gender, deptId, sessionId, contact, email, address, password1));
            if (userId != -1) {
                Util.saveImageToInternalStorage(this, imageUri, String.valueOf(database.getAccountType("pendingStudent").accountId) + userId + ".jpeg");
                Toast.generalSuccess(this, "Signup completed. Wait for admin to verify");
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
        setContentView(R.layout.activity_student_signup);
        Calendar calendar = Calendar.getInstance();
        database = StudentDBHandler.getInstance(this);
        EditText studentSignupNameInput, studentSignupDOBInput, studentSignupContactInput, studentSignupEmailInput,
                studentSignupAddressInput, studentSignupPwInput, studentSignupRetypePwInput;
        Spinner studentSignupGenderSpinner, studentSignupDeptSpinner, studentSignupSessionSpinner;
        AppCompatButton studentSignupSignupButton;

        studentSignupNameInput = findViewById(R.id.studentSignupNameInput);
        studentSignupDOBInput = findViewById(R.id.studentSignupDOBInput);
        studentSignupGenderSpinner = findViewById(R.id.studentSignupGenderSpinner);
        studentSignupDeptSpinner = findViewById(R.id.studentSignupDeptSpinner);
        studentSignupSessionSpinner = findViewById(R.id.studentSignupSessionSpinner);
        studentSignupContactInput = findViewById(R.id.studentSignupContactInput);
        studentSignupEmailInput = findViewById(R.id.studentSignupEmailInput);
        studentSignupAddressInput = findViewById(R.id.studentSignupAddressInput);
        studentSignupPwInput = findViewById(R.id.studentSignupPwInput);
        studentSignupRetypePwInput = findViewById(R.id.studentSignupRetypePwInput);
        studentSignupImageSelectorButton = findViewById(R.id.studentSignupImageSelectorButton);
        studentSignupImagePreviewer = findViewById(R.id.studentSignupImagePreviewer);
        studentSignupSignupButton = findViewById(R.id.studentSignupSignupButton);

        ArrayList<SessionInfo> gender = new ArrayList<>();
        gender.add(new SessionInfo(-1, "Gender"));
        gender.add(new SessionInfo(1, "Male"));
        gender.add(new SessionInfo(2, "Female"));
        SessionSelectorAdapter genderSelectorAdapter = new SessionSelectorAdapter(this, gender);
        studentSignupGenderSpinner.setAdapter(genderSelectorAdapter);
        studentSignupGenderSpinner.setSelection(0);

        ArrayList<DeptInfo> departments = new ArrayList<>(database.getDepartments());
        if (departments.get(0).deptId != -1) departments.add(0, new DeptInfo(-1, "Department", ""));
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, departments);
        studentSignupDeptSpinner.setAdapter(deptSelectorAdapter);
        studentSignupDeptSpinner.setSelection(0);

        ArrayList<SessionInfo> sessions = new ArrayList<>(database.getSessions());
        if (sessions.get(0).sessionId != -1) {
            sessions.add(0, new SessionInfo(-1, "Session"));
        }
        SessionSelectorAdapter sessionSelectorAdapter = new SessionSelectorAdapter(this, sessions);
        studentSignupSessionSpinner.setAdapter(sessionSelectorAdapter);
        studentSignupSessionSpinner.setSelection(0);

        Util.togglePasswordVisibility(this, studentSignupPwInput);
        Util.togglePasswordVisibility(this, studentSignupRetypePwInput);
        studentSignupImagePreviewer.setVisibility(View.GONE);

        studentSignupImageSelectorButton.setOnClickListener(v -> {
            if (!PermissionHandler.checkReadStoragePermission(this)) {
                PermissionHandler.requestReadStoragePermission(this);
                if (!PermissionHandler.checkReadStoragePermission(this)) return;
            }
            Util.openImagePicker(this);
        });
        studentSignupImagePreviewer.setOnClickListener(v -> Util.openImagePicker(this));
        studentSignupDOBInput.setOnClickListener(v -> Util.pickDate(this, calendar, studentSignupDOBInput));
        studentSignupSignupButton.setOnClickListener(v -> {
            name = studentSignupNameInput.getText().toString().trim();
            dob = studentSignupDOBInput.getText().toString().trim();
            genderId = (int) studentSignupGenderSpinner.getSelectedItemId();
            deptId = (int) studentSignupDeptSpinner.getSelectedItemId();
            sessionId = (int) studentSignupSessionSpinner.getSelectedItemId();
            contact = studentSignupContactInput.getText().toString().trim();
            email = studentSignupEmailInput.getText().toString().trim();
            address = studentSignupAddressInput.getText().toString().trim();
            password1 = studentSignupPwInput.getText().toString().trim();
            password2 = studentSignupRetypePwInput.getText().toString().trim();

            if (name.isBlank()) {
                Toast.generalError(this, "Name field is empty");
                return;
            }
            if (dob.isBlank()) {
                Toast.generalError(this, "Date of birth field is empty");
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
            if (sessionId == -1) {
                Toast.generalError(this, "Select session");
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
            if (database.isEmailExists(deptId, email)) {
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
                Bitmap imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                if (imageBitmap.getWidth() != 256 || imageBitmap.getHeight() != 256)
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 256, 256, true);
                studentSignupImageSelectorButton.setVisibility(View.GONE);
                studentSignupImagePreviewer.setVisibility(View.VISIBLE);
                studentSignupImagePreviewer.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                Toast.generalError(this, "Failed to load image");
            }
        }
    }
}