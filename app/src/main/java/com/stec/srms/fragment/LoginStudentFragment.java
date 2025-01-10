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
import com.stec.srms.activity.StudentInfoActivity;
import com.stec.srms.activity.StudentSignupActivity;

public class LoginStudentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_student, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        EditText loginStudentIdInput, loginStudentPwInput;
        AppCompatButton loginStudentLoginButton, loginStudentForgetPwButton, loginStudentCreateAccountButton;

        loginStudentIdInput = view.findViewById(R.id.loginStudentIdInput);
        loginStudentPwInput = view.findViewById(R.id.loginStudentPwInput);
        loginStudentLoginButton = view.findViewById(R.id.loginStudentLoginButton);
        loginStudentForgetPwButton = view.findViewById(R.id.loginStudentForgetPwButton);
        loginStudentCreateAccountButton = view.findViewById(R.id.loginStudentCreateAccountButton);

        loginStudentLoginButton.setOnClickListener(v -> {
            // All checkings
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

        makePasswordShowable(loginStudentPwInput, context);
        return view;
    }
}