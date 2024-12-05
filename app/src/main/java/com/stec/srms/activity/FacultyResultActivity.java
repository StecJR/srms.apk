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

import java.util.ArrayList;

public class FacultyResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_result);

        Spinner departmentSpinner = findViewById(R.id.teacherResultDepartmentSpinner);
        Spinner sessionSpinner = findViewById(R.id.teacherResultSessionSpinner);
        Spinner examSpinner = findViewById(R.id.teacherResultExamSpinner);
        Spinner courseSpinner = findViewById(R.id.teacherResultCourseSpinner);

        ArrayList<String> departments = new ArrayList<>();
        departments.add("Department");
        departments.add("CSE");
        departments.add("EEE");
        departments.add("Textile");
        departments.add("FAD");
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments) {
            @Override
            public View getDropDownView(int position, View view, ViewGroup parent) {
                if (position == 0) {
                    TextView hiddenView = new TextView(getContext());
                    hiddenView.setVisibility(View.GONE);
                    return hiddenView;
                } else {
                    return super.getDropDownView(position, view, parent);
                }
            }
        };
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        departmentSpinner.setAdapter(departmentAdapter);
        departmentSpinner.setSelection(0);

        ArrayList<String> sessions = new ArrayList<>();
        sessions.add("Session");
        sessions.add("2021-2022");
        sessions.add("2022-2023");
        sessions.add("2023-2024");
        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessions) {
            @Override
            public View getDropDownView(int position, View view, ViewGroup parent) {
                if (position == 0) {
                    TextView hiddenView = new TextView(getContext());
                    hiddenView.setVisibility(View.GONE);
                    return hiddenView;
                } else {
                    return super.getDropDownView(position, view, parent);
                }
            }
        };
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sessionSpinner.setAdapter(sessionAdapter);
        sessionSpinner.setSelection(0);

        ArrayList<String> exams = new ArrayList<>();
        exams.add("Exam");
        exams.add("1st Year 1st Semester");
        exams.add("1st Year 2nd Semester");
        exams.add("2nd Year 1st Semester");
        exams.add("2nd Year 2nd Semester");
        exams.add("3rd Year 1st Semester");
        exams.add("3rd Year 2nd Semester");
        exams.add("4th Year 1st Semester");
        exams.add("4th Year 2nd Semester");
        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exams) {
            @Override
            public View getDropDownView(int position, View view, ViewGroup parent) {
                if (position == 0) {
                    TextView hiddenView = new TextView(getContext());
                    hiddenView.setVisibility(View.GONE);
                    return hiddenView;
                } else {
                    return super.getDropDownView(position, view, parent);
                }
            }
        };
        examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        examSpinner.setAdapter(examAdapter);
        examSpinner.setSelection(0);

        ArrayList<String> courses = new ArrayList<>();
        courses.add("Course");
        courses.add("EDC");
        courses.add("DSA");
        courses.add("DEPT");
        courses.add("PTA");
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses) {
            @Override
            public View getDropDownView(int position, View view, ViewGroup parent) {
                if (position == 0) {
                    TextView hiddenView = new TextView(getContext());
                    hiddenView.setVisibility(View.GONE);
                    return hiddenView;
                } else {
                    return super.getDropDownView(position, view, parent);
                }
            }
        };
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseSpinner.setAdapter(courseAdapter);
        courseSpinner.setSelection(0);
    }
}