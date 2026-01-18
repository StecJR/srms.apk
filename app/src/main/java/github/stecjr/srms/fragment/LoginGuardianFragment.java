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
import github.stecjr.srms.activity.GuardianInfoActivity;
import github.stecjr.srms.activity.GuardianSignupActivity;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.model.GuardianInfo;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Toast;

public class LoginGuardianFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_guardian, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        StudentDBHandler guardianDBHandler = StudentDBHandler.getInstance(context);
        EditText loginGuardianIdInput, loginGuardianPwInput;
        AppCompatButton loginGuardianLoginButton, loginGuardianCreateAccountButton;

        loginGuardianIdInput = view.findViewById(R.id.loginGuardianIdInput);
        loginGuardianPwInput = view.findViewById(R.id.loginGuardianPwInput);
        loginGuardianLoginButton = view.findViewById(R.id.loginGuardianLoginButton);
        loginGuardianCreateAccountButton = view.findViewById(R.id.loginGuardianCreateAccountButton);

        loginGuardianLoginButton.setOnClickListener(v -> {
            int guardianId = stringToInt(loginGuardianIdInput.getText().toString().trim(), -1);
            String guardianPw = loginGuardianPwInput.getText().toString().trim();
            boolean isValid = guardianId != -1 &&
                    !guardianPw.isBlank() &&
                    guardianDBHandler.isValidGuardian(guardianId, guardianPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }

            GuardianInfo guardianInfo = guardianDBHandler.getGuardianinfo(guardianId);
            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createGuardianSession(guardianInfo.guardianId, guardianInfo.deptId, guardianInfo.studentId, 15);
            startActivity(new Intent(context, GuardianInfoActivity.class));
            activity.finish();
        });
        loginGuardianCreateAccountButton.setOnClickListener(v -> startActivity(new Intent(context, GuardianSignupActivity.class)));

        togglePasswordVisibility(context, loginGuardianPwInput);
        return view;
    }
}