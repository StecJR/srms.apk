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

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import github.stecjr.srms.R;
import github.stecjr.srms.activity.FacultyInfoActivity;
import github.stecjr.srms.activity.FacultySignupActivity;
import github.stecjr.srms.database.FacultyDBHandler;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Toast;

public class LoginFacultyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_faculty, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        FacultyDBHandler facultyDBHandler = FacultyDBHandler.getInstance(context);
        EditText loginFacultyIdInput, loginFacultyPwInput;
        AppCompatButton loginFacultyLoginButton, loginFacultyCreateAccountButton;

        loginFacultyIdInput = view.findViewById(R.id.loginFacultyIdInput);
        loginFacultyPwInput = view.findViewById(R.id.loginFacultyPwInput);
        loginFacultyLoginButton = view.findViewById(R.id.loginFacultyButton);
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

            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createFacultySession(stringToInt(facultyId, -1), 15);
            startActivity(new Intent(context, FacultyInfoActivity.class));
            activity.finish();
        });
        loginFacultyCreateAccountButton.setOnClickListener(v -> startActivity(new Intent(context, FacultySignupActivity.class)));

        togglePasswordVisibility(context, loginFacultyPwInput);
        return view;
    }
}