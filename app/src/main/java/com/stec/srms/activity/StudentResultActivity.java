package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;
import com.stec.srms.database.StudentDBHandler;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.model.ResultsSummary;
import com.stec.srms.model.StudentInfo;
import com.stec.srms.model.StudentSession;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Util;

import java.util.ArrayList;

public class StudentResultActivity extends AppCompatActivity {
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createTableRow(TableLayout tableLayout, int semesterId, String semesterDesc, String gpa, String cgpa) {
        TableRow tableRow;
        TableRow.LayoutParams weightParam, widthParam;
        TextView examTextView, gpaTextView, cgpaTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        widthParam = new TableRow.LayoutParams(Util.dpToPx(this, 60), TableRow.LayoutParams.WRAP_CONTENT);
        tableRow = new TableRow(tableRowStyle);
        tableRow.setPadding(tableRow.getPaddingLeft(), 12, tableRow.getPaddingRight(), 12);

        examTextView = new TextView(tableRowTextStyle);
        examTextView.setLayoutParams(weightParam);
        examTextView.setText(semesterDesc);
        examTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(examTextView);

        gpaTextView = new TextView(tableRowTextStyle);
        gpaTextView.setLayoutParams(widthParam);
        gpaTextView.setText(gpa);
        gpaTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(gpaTextView);

        cgpaTextView = new TextView(tableRowTextStyle);
        cgpaTextView.setLayoutParams(widthParam);
        cgpaTextView.setText(cgpa);
        cgpaTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(cgpaTextView);

        tableRow.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudentMarkSheetActivity.class);
            intent.putExtra("semesterId", semesterId);
            startActivity(intent);
        });
        tableLayout.addView(tableRow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_result);
        Intent intent = getIntent();
        boolean hideTopButtons = intent.getBooleanExtra("hideTopButtons", false);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(getApplicationContext());
        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());

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
                    startActivity(new Intent(this, AdminInfo.class));
                    break;
                default:
                    startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
            return;
        }

        // Handle invalid student session
        StudentSession studentSession = sessionManager.getStudentSession();
        if (studentSession == null ||
                !studentDBHandler.isValidStudent(studentSession.deptId, studentSession.studentId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TableLayout table = findViewById(R.id.studentResultTable);
        StudentInfo studentInfo = studentDBHandler.getStudentinfo(studentSession.deptId, studentSession.studentId);
        ArrayList<ResultsSummary> resultsSummaries = studentDBHandler.getResultsSummaries(
                studentInfo.sessionId, studentInfo.deptId, studentInfo.studentId);

        for (ResultsSummary summary : resultsSummaries) {
            createTableRow(table, summary.semesterId, studentDBHandler.getSemester(summary.semesterId).longDesc, String.valueOf(summary.gpa), String.valueOf(summary.cgpa));
        }
    }
}