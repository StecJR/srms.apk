package github.stecjr.srms.fragment;

import static github.stecjr.srms.util.Util.stringToInt;
import static github.stecjr.srms.util.Util.togglePasswordVisibility;

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

import java.util.ArrayList;

import github.stecjr.srms.R;
import github.stecjr.srms.activity.StudentInfoActivity;
import github.stecjr.srms.activity.StudentSignupActivity;
import github.stecjr.srms.adapter.DeptSelectorAdapter;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.model.DeptInfo;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Toast;

public class LoginStudentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_student, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(context);
        Spinner departmentSpinner;
        EditText loginStudentIdInput, loginStudentPwInput;
        AppCompatButton loginStudentLoginButton, loginStudentCreateAccountButton;

        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        loginStudentIdInput = view.findViewById(R.id.loginStudentIdInput);
        loginStudentPwInput = view.findViewById(R.id.loginStudentPwInput);
        loginStudentLoginButton = view.findViewById(R.id.loginStudentLoginButton);
        loginStudentCreateAccountButton = view.findViewById(R.id.loginStudentCreateAccountButton);

        ArrayList<DeptInfo> departments = new ArrayList<>(studentDBHandler.getDepartments());
        if (departments.get(0).deptId != -1) departments.add(0, new DeptInfo(-1, "Department", ""));
        DeptSelectorAdapter deptSelectorAdapter = new DeptSelectorAdapter(context, departments);
        departmentSpinner.setAdapter(deptSelectorAdapter);
        departmentSpinner.setSelection(0);

        loginStudentLoginButton.setOnClickListener(v -> {
            int deptId = (int) departmentSpinner.getSelectedItemId();
            if (deptId == -1) {
                Toast.generalError(context, "Select your department");
                return;
            }
            String studentId = loginStudentIdInput.getText().toString().trim();
            String studentPw = loginStudentPwInput.getText().toString().trim();
            boolean isValid = !studentId.isBlank() && !studentPw.isBlank() &&
                    studentDBHandler.isValidStudent(context, deptId, stringToInt(studentId, -1), studentPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }

            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createStudentSession(deptId, stringToInt(studentId, -1), 15);
            startActivity(new Intent(context, StudentInfoActivity.class));
            activity.finish();
        });
        loginStudentCreateAccountButton.setOnClickListener(v -> startActivity(new Intent(context, StudentSignupActivity.class)));

        togglePasswordVisibility(context, loginStudentPwInput);
        return view;
    }
}