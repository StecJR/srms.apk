package github.stecjr.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Locale;

import github.stecjr.srms.R;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.model.AdminInfo;
import github.stecjr.srms.model.MarkSheetData;
import github.stecjr.srms.model.Results;
import github.stecjr.srms.model.SemesterInfo;
import github.stecjr.srms.model.StudentInfo;
import github.stecjr.srms.model.StudentSession;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Toast;
import github.stecjr.srms.util.Util;

public class StudentMarkSheetActivity extends AppCompatActivity {
    private ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createMarkSheetTable(TableLayout tableLayout, ArrayList<MarkSheetData> markSheet) {
        TableRow tableRow;
        TableRow.LayoutParams weightParam, widthParam;
        TextView codeTextView, courseTextView, gpaTextView, gradeTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        widthParam = new TableRow.LayoutParams(Util.dpToPx(this, 50), TableRow.LayoutParams.WRAP_CONTENT);

        for (MarkSheetData markSheetData : markSheet) {
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
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_SimpleRowText);
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        Intent intent = getIntent();
        int semesterId = intent.getIntExtra("semesterId", -1);

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
        if (studentSession == null || !studentDBHandler.isValidStudent(this, studentSession.deptId, studentSession.studentId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TableLayout table = findViewById(R.id.studentMarkSheetTable);
        TextView title = findViewById(R.id.studentMarkSheetTitle);
        StudentInfo studentInfo = studentDBHandler.getStudentinfo(studentSession.deptId, studentSession.studentId);
        ArrayList<Results> results = studentDBHandler.getResults(studentInfo.sessionId, studentInfo.deptId, studentInfo.studentId, semesterId);
        SemesterInfo semesterInfo = studentDBHandler.getSemester(semesterId);
        title.setText(semesterInfo.longDesc);

        ArrayList<MarkSheetData> markSheet = new ArrayList<>();
        for (Results result : results) {
            markSheet.add(new MarkSheetData(
                    String.valueOf(result.courseCode),
                    studentDBHandler.getCourse(result.courseCode).longDesc,
                    String.valueOf(result.mark),
                    String.format(Locale.getDefault(), "%.2f", result.gpa),
                    Util.getGrade(result.gpa)
            ));
        }

        createMarkSheetTable(table, markSheet);

        AppCompatButton studentMarkSheetPrintButton = findViewById(R.id.studentMarkSheetPrintButton);
        studentMarkSheetPrintButton.setOnClickListener(v -> {
            Util.saveMarkSheetAsPDF(this, semesterInfo.longDesc, studentInfo, markSheet);
            Toast.generalSuccess(this, "Mark sheet saved successfully");
        });
    }
}