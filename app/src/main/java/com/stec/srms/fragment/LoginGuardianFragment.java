package com.stec.srms.fragment;

import static com.stec.srms.util.Util.makePasswordShowable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stec.srms.R;
import com.stec.srms.activity.GuardianInfoActivity;
import com.stec.srms.activity.GuardianSignupActivity;
import com.stec.srms.database.GuardianDBHandler;
import com.stec.srms.util.Toast;

public class LoginGuardianFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_guardian, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        GuardianDBHandler guardianDBHandler = GuardianDBHandler.getInstance(context);
        EditText loginGuardianIdInput, loginGuardianPwInput;
        AppCompatButton loginGuardianLoginButton, loginGuardianForgetPwButton, loginGuardianCreateAccountButton;

        loginGuardianIdInput = view.findViewById(R.id.loginGuardianIdInput);
        loginGuardianPwInput = view.findViewById(R.id.loginGuardianPwInput);
        loginGuardianLoginButton = view.findViewById(R.id.loginGuardianLoginButton);
        loginGuardianForgetPwButton = view.findViewById(R.id.loginGuardianForgetPwButton);
        loginGuardianCreateAccountButton = view.findViewById(R.id.loginGuardianCreateAccountButton);

        loginGuardianLoginButton.setOnClickListener(v -> {
            String guardianId = loginGuardianIdInput.getText().toString().trim();
            String guardianPw = loginGuardianPwInput.getText().toString().trim();
            boolean isValid = !guardianId.isBlank() &&
                    !guardianPw.isBlank() &&
                    guardianDBHandler.isValidGuardian(guardianId, guardianPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }
            // Save session info

            startActivity(new Intent(context, GuardianInfoActivity.class));
            requireActivity().finish();
        });
        loginGuardianForgetPwButton.setOnClickListener(v -> {
            // startActivity(new Intent(context, StudentInfoActivity.class));
            // activity.finish();
        });
        loginGuardianCreateAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(context, GuardianSignupActivity.class));
        });

        makePasswordShowable(loginGuardianPwInput, context);
        return view;
    }
}