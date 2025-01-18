package com.stec.srms.fragment;

import static com.stec.srms.util.Util.addPasswordVisibilityToggler;
import static com.stec.srms.util.Util.stringToInt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.stec.srms.R;
import com.stec.srms.activity.StudentInfoActivity;
import com.stec.srms.activity.StudentSignupActivity;
import com.stec.srms.adapter.DeptSelectorAdapter;
import com.stec.srms.database.StudentDBHandler;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;

public class LoginStudentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_student, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(context);
        Spinner departmentSpinner;
        EditText loginStudentIdInput, loginStudentPwInput;
        AppCompatButton loginStudentLoginButton, loginStudentForgetPwButton, loginStudentCreateAccountButton;

        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        loginStudentIdInput = view.findViewById(R.id.loginStudentIdInput);
        loginStudentPwInput = view.findViewById(R.id.loginStudentPwInput);
        loginStudentLoginButton = view.findViewById(R.id.loginStudentLoginButton);
        loginStudentForgetPwButton = view.findViewById(R.id.loginStudentForgetPwButton);
        loginStudentCreateAccountButton = view.findViewById(R.id.loginStudentCreateAccountButton);

        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(context, studentDBHandler.getDepartments());
        departmentSpinner.setAdapter(deptSelectorAdapter);
        departmentSpinner.setSelection(0);

        loginStudentLoginButton.setOnClickListener(v -> {
            int deptId = (int) departmentSpinner.getSelectedItemId();
            String studentId = loginStudentIdInput.getText().toString().trim();
            String studentPw = loginStudentPwInput.getText().toString().trim();
            boolean isValid = deptId != 0 &&
                    !studentId.isBlank() &&
                    !studentPw.isBlank() &&
                    studentDBHandler.isValidStudent(deptId, stringToInt(studentId, -1), studentPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }
            // Save session info
            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createStudentSession(deptId, stringToInt(studentId, -1), 15);
            startActivity(new Intent(context, StudentInfoActivity.class));
            activity.finish();
        });
        loginStudentForgetPwButton.setOnClickListener(v -> {
            // startActivity(new Intent(context, StudentInfoActivity.class));
            // activity.finish();
        });
        loginStudentCreateAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(context, StudentSignupActivity.class));
        });

        addPasswordVisibilityToggler(loginStudentPwInput, context);
        return view;
    }
}