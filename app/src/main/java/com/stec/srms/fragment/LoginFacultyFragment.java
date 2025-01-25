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

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.stec.srms.R;
import com.stec.srms.activity.FacultyInfoActivity;
import com.stec.srms.activity.FacultySignupActivity;
import com.stec.srms.database.FacultyDBHandler;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;

public class LoginFacultyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_faculty, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(context);
        EditText loginFacultyIdInput, loginFacultyPwInput;
        AppCompatButton loginFacultyLoginButton, loginFacultyForgetPwButton, loginFacultyCreateAccountButton;

        loginFacultyIdInput = view.findViewById(R.id.loginFacultyIdInput);
        loginFacultyPwInput = view.findViewById(R.id.loginFacultyPwInput);
        loginFacultyLoginButton = view.findViewById(R.id.loginFacultyButton);
        loginFacultyForgetPwButton = view.findViewById(R.id.loginFacultyForgetPwButton);
        loginFacultyCreateAccountButton = view.findViewById(R.id.loginFacultyCreateAccountButton);

        loginFacultyLoginButton.setOnClickListener(v -> {
            String facultyId = loginFacultyIdInput.getText().toString().trim();
            String facultyPw = loginFacultyPwInput.getText().toString().trim();
            boolean isValid = !facultyId.isBlank() &&
                    !facultyPw.isBlank() &&
                    facultyDBHandler.isValidFaculty(stringToInt(facultyId, -1), facultyPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }
            // Save session info
            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createFacultySession(stringToInt(facultyId, -1), 15);
            startActivity(new Intent(context, FacultyInfoActivity.class));
            requireActivity().finish();
        });
        loginFacultyForgetPwButton.setOnClickListener(v -> {
            // startActivity(new Intent(context, StudentInfoActivity.class));
            // activity.finish();
        });
        loginFacultyCreateAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(context, FacultySignupActivity.class));
        });

        addPasswordVisibilityToggler(loginFacultyPwInput, context);
        return view;
    }
}