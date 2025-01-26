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

    private void createTableRow(TableLayout tableLayout, int semesterId, String semesterDesc, double gpa) {
        TableRow tableRow;
        TableRow.LayoutParams weight1Param, weight2Param;
        TextView examTextView, gpaTextView;

        weight1Param = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        weight2Param = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
        tableRow = new TableRow(tableRowStyle);
        tableRow.setPadding(tableRow.getPaddingLeft(), 12, tableRow.getPaddingRight(), 12);

        examTextView = new TextView(tableRowTextStyle);
        examTextView.setLayoutParams(weight2Param);
        examTextView.setText(semesterDesc);
        examTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(examTextView);

        gpaTextView = new TextView(tableRowTextStyle);
        gpaTextView.setLayoutParams(weight1Param);
        gpaTextView.setText(String.valueOf(gpa));
        gpaTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(gpaTextView);

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
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

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
                !studentDBHandler.isValidStudent(this, studentSession.deptId, studentSession.studentId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TableLayout table = findViewById(R.id.studentResultTable);
        StudentInfo studentInfo = studentDBHandler.getStudentinfo(studentSession.deptId, studentSession.studentId);
        ArrayList<ResultsSummary> resultsSummaries = studentDBHandler.getResultsSummaries(studentInfo.sessionId, studentInfo.deptId, studentInfo.studentId);
        if (resultsSummaries == null) return;
        for (ResultsSummary summary : resultsSummaries) {
            if (summary.gpa == 0.0) continue;
            createTableRow(table, summary.semesterId, studentDBHandler.getSemester(summary.semesterId).longDesc, summary.gpa);
        }
    }
}