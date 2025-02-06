package com.stec.srms.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.stec.srms.R;
import com.stec.srms.adapter.CourseSelectorAdapter;
import com.stec.srms.adapter.DeptSelectorAdapter;
import com.stec.srms.adapter.SemesterSelectorAdapter;
import com.stec.srms.adapter.SessionSelectorAdapter;
import com.stec.srms.database.FacultyDBHandler;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.model.CourseInfo;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.FacultySession;
import com.stec.srms.model.Results;
import com.stec.srms.model.SemesterInfo;
import com.stec.srms.model.SessionInfo;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;
import com.stec.srms.util.Util;

import java.util.ArrayList;

public class FacultyResultActivity extends AppCompatActivity {
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;
    Spinner departmentSpinner, sessionSpinner, semesterSpinner, courseSpinner;
    CourseSelectorAdapter courseSelectorAdapter;
    BottomSheetDialog bottomSheetDialog;

    public void setCoursesWithHint(ArrayList<CourseInfo> courses) {
        ArrayList<CourseInfo> newCourses = new ArrayList<>(courses);
        if (newCourses.isEmpty() || newCourses.get(0).courseCode != -1) {
            newCourses.add(0, new CourseInfo(0, 0, -1, 0.0, "Course", ""));
        }
        courseSelectorAdapter = new CourseSelectorAdapter(this, newCourses);
        courseSpinner.setAdapter(courseSelectorAdapter);
        courseSpinner.setSelection(0);
    }

    private void openMarkEditor(Results result, TableRow tableRow) {
        bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams")
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_mark, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText bottomSheetEditMarkInput;
        AppCompatButton bottomSheetEditMarkCancelButton, bottomSheetEditMarkSaveButton;

        bottomSheetEditMarkInput = bottomSheetView.findViewById(R.id.bottomSheetEditMarkInput);
        bottomSheetEditMarkCancelButton = bottomSheetView.findViewById(R.id.bottomSheetEditMarkCancelButton);
        bottomSheetEditMarkSaveButton = bottomSheetView.findViewById(R.id.bottomSheetEditMarkSaveButton);

        bottomSheetEditMarkInput.setText(String.valueOf(result.mark));
        bottomSheetEditMarkCancelButton.setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetEditMarkSaveButton.setOnClickListener(view -> {
            try {
                int mark = Integer.parseInt(bottomSheetEditMarkInput.getText().toString());
                if (mark != result.mark) {
                    result.mark = mark;
                    result.gpa = Util.getGpa(mark);

                    TextView markTextView = (TextView) tableRow.getChildAt(1);
                    TextView gradeTextView = (TextView) tableRow.getChildAt(2);
                    markTextView.setText(String.valueOf(result.mark));
                    gradeTextView.setText(Util.getGrade(result.gpa));

                    FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(this);
                    facultyDBHandler.updateResult(
                            this,
                            (int) sessionSpinner.getSelectedItemId(),
                            (int) departmentSpinner.getSelectedItemId(),
                            result
                    );
                    facultyDBHandler.regenerateGpa(
                            this,
                            (int) sessionSpinner.getSelectedItemId(),
                            (int) departmentSpinner.getSelectedItemId(),
                            result.studentId,
                            result.semesterId
                    );
                }
            } catch (NumberFormatException e) {
                Toast.generalError(this, "Invalid mark");
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void createTableRows(TableLayout tableLayout, ArrayList<Results> results) {
        if (results == null || results.isEmpty()) return;
        TableRow tableRow;
        TableRow.LayoutParams weightParam, widthParam;
        TextView studentIdTextView, markTextView, gradeTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        widthParam = new TableRow.LayoutParams(Util.dpToPx(this, 60), TableRow.LayoutParams.WRAP_CONTENT);

        for (Results result : results) {
            tableRow = new TableRow(tableRowStyle);

            studentIdTextView = new TextView(tableRowTextStyle);
            studentIdTextView.setLayoutParams(weightParam);
            studentIdTextView.setText(String.valueOf(result.studentId));
            studentIdTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
            tableRow.addView(studentIdTextView);

            markTextView = new TextView(tableRowTextStyle);
            markTextView.setLayoutParams(widthParam);
            markTextView.setText(String.valueOf(result.mark));
            markTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(markTextView);

            gradeTextView = new TextView(tableRowTextStyle);
            gradeTextView.setLayoutParams(widthParam);
            gradeTextView.setText(Util.getGrade(result.gpa));
            gradeTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(gradeTextView);

            TableRow finalTableRow = tableRow;
            tableRow.setOnClickListener(view -> openMarkEditor(result, finalTableRow));
            tableLayout.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_result);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        AppCompatButton facultyResultSearchButton;

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else if (!accountType.equals("faculty")) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "guardian":
                    startActivity(new Intent(this, GuardianInfoActivity.class));
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
        FacultySession facultySession = sessionManager.getFacultySession();
        if (facultySession == null ||
                !facultyDBHandler.isValidFaculty(facultySession.facultyId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        departmentSpinner = findViewById(R.id.departmentSpinner);
        sessionSpinner = findViewById(R.id.sessionSpinner);
        semesterSpinner = findViewById(R.id.semesterSpinner);
        courseSpinner = findViewById(R.id.courseSpinner);
        facultyResultSearchButton = findViewById(R.id.facultyResultSearchButton);

        ArrayList<DeptInfo> departments = new ArrayList<>(facultyDBHandler.getDepartments());
        if (departments.get(0).deptId != -1) {
            departments.add(0, new DeptInfo(-1, "Department", ""));
        }
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, departments);
        departmentSpinner.setAdapter(deptSelectorAdapter);
        departmentSpinner.setSelection(0);

        ArrayList<SessionInfo> sessions = new ArrayList<>(facultyDBHandler.getSessions());
        if (sessions.get(0).sessionId != -1) {
            sessions.add(0, new SessionInfo(-1, "Session"));
        }
        SessionSelectorAdapter sessionSelectorAdapter = new SessionSelectorAdapter(this, sessions);
        sessionSpinner.setAdapter(sessionSelectorAdapter);
        sessionSpinner.setSelection(0);

        ArrayList<SemesterInfo> semesters = new ArrayList<>(facultyDBHandler.getSemesters());
        if (semesters.get(0).semesterId != -1) {
            semesters.add(0, new SemesterInfo(-1, "Semester", ""));
        }
        SemesterSelectorAdapter semesterSelectorAdapter = new SemesterSelectorAdapter(this, semesters);
        semesterSpinner.setAdapter(semesterSelectorAdapter);
        semesterSpinner.setSelection(0);
        setCoursesWithHint(facultyDBHandler.getCourses());

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0 && semesterSpinner.getSelectedItemId() != 0) {
                    setCoursesWithHint(facultyDBHandler.getSemesterCourses((int) id, (int) semesterSpinner.getSelectedItemId()));
                } else if (id != 0) {
                    setCoursesWithHint(facultyDBHandler.getDepartmentCourses((int) id));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0 && departmentSpinner.getSelectedItemId() != 0) {
                    setCoursesWithHint(facultyDBHandler.getSemesterCourses((int) departmentSpinner.getSelectedItemId(), (int) id));
                } else if (id != 0) {
                    setCoursesWithHint(facultyDBHandler.getSemesterCourses((int) id));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        facultyResultSearchButton.setOnClickListener(v -> {
            int deptId = (int) departmentSpinner.getSelectedItemId();
            int sessionId = (int) sessionSpinner.getSelectedItemId();
            int semesterId = (int) semesterSpinner.getSelectedItemId();
            int courseCode = (int) courseSpinner.getSelectedItemId();

            if (deptId == -1) {
                Toast.generalError(this, "Select department");
                return;
            }
            if (sessionId == -1) {
                Toast.generalError(this, "Select session");
                return;
            }
            if (semesterId == -1) {
                Toast.generalError(this, "Select semester");
                return;
            }
            if (courseCode == -1) {
                Toast.generalError(this, "Select course");
                return;
            }

            TableLayout table = findViewById(R.id.facultyResultResultTable);
            for (int i = table.getChildCount() - 1; i > 0; i--) {
                table.removeViewAt(i);
            }

            if (!facultyDBHandler.isTableExists("SELECT * FROM results_" + sessionId + "_" + deptId + " LIMIT 1;")) {
                Context context = this;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to create a new result record?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        facultyDBHandler.createNewResultTable(context, sessionId, deptId, semesterId, courseCode);
                    } catch (Exception e) {
                        Toast.databaseInfo(context, "No student record found");
                    }
                    createTableRows(table, facultyDBHandler.getCourseResult(sessionId, deptId, courseCode));
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ArrayList<Results> results = facultyDBHandler.getCourseResult(sessionId, deptId, courseCode);
                if (results == null || results.isEmpty()) {
                    Context context = this;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to create a new result?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        facultyDBHandler.addNewCourseResult(context, sessionId, deptId, semesterId, courseCode);
                        if (!facultyDBHandler.hasSemesterInResultSummary(sessionId, deptId, semesterId)) {
                            facultyDBHandler.addNewCourseResultSummary(context, sessionId, deptId, semesterId);
                        }
                        createTableRows(table, facultyDBHandler.getCourseResult(sessionId, deptId, courseCode));
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    createTableRows(table, results);
                }
            }
        });
    }
}