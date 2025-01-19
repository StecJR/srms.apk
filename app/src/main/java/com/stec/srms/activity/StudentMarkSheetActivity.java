package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;
import com.stec.srms.database.StudentDBHandler;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.model.MarkSheetData;
import com.stec.srms.model.Results;
import com.stec.srms.model.SemesterInfo;
import com.stec.srms.model.StudentInfo;
import com.stec.srms.model.StudentSession;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

import java.util.ArrayList;
import java.util.Locale;

public class StudentMarkSheetActivity extends AppCompatActivity {
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private String getGrade(double gpa) {
        if (gpa >= 4.0) return "A+";
        else if (gpa >= 3.75) return "A";
        else if (gpa >= 3.5) return "A-";
        else if (gpa >= 3.25) return "B+";
        else if (gpa >= 3.0) return "B";
        else if (gpa >= 2.75) return "B-";
        else if (gpa >= 2.5) return "C+";
        else if (gpa >= 2.25) return "C";
        else if (gpa >= 2.0) return "C-";
        else return "F";
    }

    private void createMarkSheetTable(TableLayout tableLayout, ArrayList<MarkSheetData> markSheet) {
        for (MarkSheetData markSheetData : markSheet) {
            TableRow tableRow;
            TableRow.LayoutParams weightParam, widthParam;
            TextView codeTextView, courseTextView, gpaTextView, gradeTextView;

            weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            widthParam = new TableRow.LayoutParams(Util.dpToPx(this, 50), TableRow.LayoutParams.WRAP_CONTENT);
            tableRow = new TableRow(tableRowStyle);

            codeTextView = new TextView(tableRowTextStyle);
            codeTextView.setText(markSheetData.courseCode);
            codeTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(codeTextView);

            courseTextView = new TextView(tableRowTextStyle);
            courseTextView.setLayoutParams(weightParam);
            courseTextView.setText(markSheetData.courseDesc);
            courseTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(courseTextView);

            gpaTextView = new TextView(tableRowTextStyle);
            gpaTextView.setLayoutParams(widthParam);
            gpaTextView.setText(markSheetData.gpa);
            gpaTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(gpaTextView);

            gradeTextView = new TextView(tableRowTextStyle);
            gradeTextView.setLayoutParams(widthParam);
            gradeTextView.setText(markSheetData.grade);
            gradeTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(gradeTextView);

            tableLayout.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_marksheet);
        Intent intent = getIntent();
        int semesterId = intent.getIntExtra("semesterId", -1);
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
                !studentDBHandler.isValidStudent(studentSession.deptId, studentSession.studentId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TableLayout table = findViewById(R.id.studentMarksheetTable);
        TextView title = findViewById(R.id.studentMarksheetTitle);
        StudentInfo studentInfo = studentDBHandler.getStudentinfo(studentSession.deptId, studentSession.studentId);
        ArrayList<Results> results = studentDBHandler.getResults(
                studentInfo.sessionId, studentInfo.deptId, studentInfo.studentId, semesterId
        );
        SemesterInfo semesterInfo = studentDBHandler.getSemester(semesterId);
        title.setText(semesterInfo.longDesc);

        ArrayList<MarkSheetData> markSheet = new ArrayList<>();
        for (Results result : results) {
            markSheet.add(new MarkSheetData(
                    String.valueOf(result.courseCode),
                    studentDBHandler.getCourse(result.courseCode).longDesc,
                    String.valueOf(result.mark),
                    String.format(Locale.getDefault(), "%.2f", result.gpa),
                    getGrade(result.gpa)
            ));
        }

        createMarkSheetTable(table, markSheet);

        AppCompatButton studentMarkSheetPrintButton = findViewById(R.id.studentMarkSheetPrintButton);
        studentMarkSheetPrintButton.setOnClickListener(v -> {
            if (!Util.checkStoragePermission(this)) {
                Util.requestStoragePermission(this);
                if (!Util.checkStoragePermission(this)) {
                    Toast.generalError(this, "Storage permission is required to save mark sheet");
                    return;
                }
            }
            Util.saveMarkSheetAsPDF(this, semesterInfo.longDesc, studentInfo, markSheet);
            Toast.generalSuccess(this, "Mark sheet saved successfully");
        });

    }
}