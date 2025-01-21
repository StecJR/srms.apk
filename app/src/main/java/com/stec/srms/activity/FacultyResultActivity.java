package com.stec.srms.activity;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;
import com.stec.srms.adapter.CourseSelectorAdapter;
import com.stec.srms.adapter.DeptSelectorAdapter;
import com.stec.srms.adapter.SemesterSelectorAdapter;
import com.stec.srms.adapter.SessionSelectorAdapter;
import com.stec.srms.database.FacultyDBHandler;
import com.stec.srms.model.CourseInfo;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.SemesterInfo;
import com.stec.srms.model.SessionInfo;

import java.util.ArrayList;

public class FacultyResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_result);

        FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(this);
        Spinner departmentSpinner, sessionSpinner, semesterSpinner, courseSpinner;

        departmentSpinner = findViewById(R.id.departmentSpinner);
        sessionSpinner = findViewById(R.id.sessionSpinner);
        semesterSpinner = findViewById(R.id.semesterSpinner);
        courseSpinner = findViewById(R.id.courseSpinner);

        ArrayList<DeptInfo> departments = new ArrayList<>(facultyDBHandler.getDepartments());
        if (departments.get(0).deptId != 0) {
            departments.add(0, new DeptInfo(0, "Department", ""));
        }
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, departments);
        departmentSpinner.setAdapter(deptSelectorAdapter);
        departmentSpinner.setSelection(0);
        ArrayList<SessionInfo> sessions = new ArrayList<>(facultyDBHandler.getSessions());
        if (sessions.get(0).sessionId != 0) {
            sessions.add(0, new SessionInfo(0, "Session"));
        }
        SessionSelectorAdapter sessionSelectorAdapter = new SessionSelectorAdapter(this, sessions);
        sessionSpinner.setAdapter(sessionSelectorAdapter);
        sessionSpinner.setSelection(0);
        ArrayList<SemesterInfo> semesters = new ArrayList<>(facultyDBHandler.getSemesters());
        if (semesters.get(0).semesterId != 0) {
            semesters.add(0, new SemesterInfo(0, "Semester", ""));
        }
        SemesterSelectorAdapter semesterSelectorAdapter = new SemesterSelectorAdapter(this, semesters);
        semesterSpinner.setAdapter(semesterSelectorAdapter);
        semesterSpinner.setSelection(0);
        ArrayList<CourseInfo> courses = new ArrayList<>(facultyDBHandler.getCourses());
        if (courses.get(0).courseCode != 0) {
            courses.add(0, new CourseInfo(0, 0, 0, 0.0, "Course", ""));
        }
        CourseSelectorAdapter courseSelectorAdapter = new CourseSelectorAdapter(this, courses);
        courseSpinner.setAdapter(courseSelectorAdapter);
        courseSpinner.setSelection(0);
    }
}