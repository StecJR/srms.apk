package github.stecjr.srms.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;

import github.stecjr.srms.R;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.model.DeptInfo;
import github.stecjr.srms.model.GuardianInfo;
import github.stecjr.srms.model.SessionInfo;
import github.stecjr.srms.model.StudentInfo;
import github.stecjr.srms.model.StudentSession;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Util;

public class StudentInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_info);
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        Intent intent = getIntent();
        boolean hideTopButtons = intent.getBooleanExtra("hideTopButtons", false);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else if (!(accountType.equals("student") || accountType.equals("guardian"))) {
            switch (accountType) {
                case "faculty":
                    startActivity(new Intent(this, FacultyInfoActivity.class));
                    break;
                case "admin":
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                    break;
                default:
                    startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
            return;
        }

        // Handle invalid student session
        StudentSession studentSession = sessionManager.getStudentSession();
        if (studentSession == null || !studentDBHandler.isValidStudent(this, studentSession.deptId, studentSession.studentId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ShapeableImageView studentInfoProfileImage;
        TextView studentInfoStudentId, studentInfoName, studentInfoBirthDate, studentInfoGender,
                studentInfoDept, studentInfoSession, studentInfoContact, studentInfoEmail, studentInfoAddress;
        StudentInfo studentInfo = studentDBHandler.getStudentinfo(studentSession.deptId, studentSession.studentId);
        DeptInfo deptInfo = studentDBHandler.getDepartment(studentInfo.deptId);
        SessionInfo sessionInfo = studentDBHandler.getSession(studentInfo.sessionId);

        studentInfoProfileImage = findViewById(R.id.studentInfoProfileImage);
        studentInfoStudentId = findViewById(R.id.studentInfoStudentId);
        studentInfoName = findViewById(R.id.studentInfoName);
        studentInfoBirthDate = findViewById(R.id.studentInfoBirthDate);
        studentInfoGender = findViewById(R.id.studentInfoGender);
        studentInfoDept = findViewById(R.id.studentInfoDept);
        studentInfoSession = findViewById(R.id.studentInfoSession);
        studentInfoContact = findViewById(R.id.studentInfoContact);
        studentInfoEmail = findViewById(R.id.studentInfoEmail);
        studentInfoAddress = findViewById(R.id.studentInfoAddress);

        int accountId = studentDBHandler.getAccountType("student").accountId;
        Bitmap profileImage = Util.getImageFromInternalStorage(this, String.valueOf(accountId) + studentInfo.studentId + ".jpeg");
        if (profileImage != null) studentInfoProfileImage.setImageBitmap(profileImage);
        else studentInfoProfileImage.setImageResource(R.drawable.default_profile);

        studentInfoStudentId.setText(String.valueOf(studentInfo.studentId));
        studentInfoName.setText(studentInfo.name);
        studentInfoBirthDate.setText(studentInfo.birthDate);
        studentInfoGender.setText(studentInfo.gender);
        studentInfoDept.setText(deptInfo.longDesc);
        studentInfoSession.setText(sessionInfo.desc);
        studentInfoContact.setText(studentInfo.contact);
        studentInfoEmail.setText(studentInfo.email);
        studentInfoAddress.setText(studentInfo.address);

        AppCompatButton studentInfoResultButton, studentInfoNoticeButton, studentInfoLogoutButton;
        studentInfoResultButton = findViewById(R.id.studentInfoResultButton);
        studentInfoNoticeButton = findViewById(R.id.studentInfoNoticeButton);
        studentInfoLogoutButton = findViewById(R.id.studentInfoLogoutButton);

        if (hideTopButtons) {
            TextView studentInfoGuardianInfoTitle = findViewById(R.id.studentInfoGuardianInfoTitle);
            TableLayout studentInfoGuardianInfoTable = findViewById(R.id.studentInfoGuardianInfoTable);
            studentInfoGuardianInfoTitle.setVisibility(View.GONE);
            studentInfoGuardianInfoTable.setVisibility(View.GONE);
            findViewById(R.id.studentInfoTopButtonLayout).setVisibility(View.GONE);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.setMargins(0, 0, 0, (int) (70 * getResources().getDisplayMetrics().density + 0.5f));
            findViewById(R.id.studentInfoTable).setLayoutParams(params);
            return;
        }

        if (studentInfo.guardianId != -1 && studentDBHandler.isValidGuardian(studentInfo.guardianId)) {
            GuardianInfo guardianInfo = studentDBHandler.getGuardianinfo(studentInfo.guardianId);
            TextView studentInfoGuardianName, studentInfoGuardianRelation, studentInfoGuardianContact, studentInfoGuardianEmail;

            studentInfoGuardianName = findViewById(R.id.studentInfoGuardianName);
            studentInfoGuardianRelation = findViewById(R.id.studentInfoGuardianRelation);
            studentInfoGuardianContact = findViewById(R.id.studentInfoGuardianContact);
            studentInfoGuardianEmail = findViewById(R.id.studentInfoGuardianEmail);

            studentInfoGuardianName.setText(guardianInfo.name);
            studentInfoGuardianRelation.setText(guardianInfo.relation);
            studentInfoGuardianContact.setText(guardianInfo.contact);
            studentInfoGuardianEmail.setText(guardianInfo.email);
        } else {
            TextView studentInfoGuardianInfoTitle = findViewById(R.id.studentInfoGuardianInfoTitle);
            TableLayout studentInfoGuardianInfoTable = findViewById(R.id.studentInfoGuardianInfoTable);
            studentInfoGuardianInfoTitle.setVisibility(View.GONE);
            studentInfoGuardianInfoTable.setVisibility(View.GONE);
        }

        studentInfoResultButton.setOnClickListener(v -> startActivity(new Intent(this, StudentResultActivity.class)));
        studentInfoNoticeButton.setOnClickListener(v -> startActivity(new Intent(this, UserNoticeBoardActivity.class)));
        studentInfoLogoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}