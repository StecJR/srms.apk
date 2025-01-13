package com.stec.srms.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;
import com.stec.srms.adapter.CourseSelectorAdapter;
import com.stec.srms.adapter.DeptSelectorAdapter;
import com.stec.srms.adapter.SemesterSelectorAdapter;
import com.stec.srms.adapter.SessionSelectorAdapter;
import com.stec.srms.database.FacultyDBHandler;

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

        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(this, facultyDBHandler.getDepartments());
        departmentSpinner.setAdapter(deptSelectorAdapter);
        departmentSpinner.setSelection(0);
        SessionSelectorAdapter sessionSelectorAdapter = new SessionSelectorAdapter(this, facultyDBHandler.getSessions());
        sessionSpinner.setAdapter(sessionSelectorAdapter);
        sessionSpinner.setSelection(0);
        SemesterSelectorAdapter semesterSelectorAdapter = new SemesterSelectorAdapter(this, facultyDBHandler.getSemesters());
        semesterSpinner.setAdapter(semesterSelectorAdapter);
        semesterSpinner.setSelection(0);
        CourseSelectorAdapter courseSelectorAdapter = new CourseSelectorAdapter(this, facultyDBHandler.getCourses());
        courseSpinner.setAdapter(courseSelectorAdapter);
        courseSpinner.setSelection(0);
    }
}