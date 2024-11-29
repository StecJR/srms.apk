package com.stec.srms.fragment;

import static com.stec.srms.util.Util.makePasswordShowable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stec.srms.R;
import com.stec.srms.activity.LoginActivity;
import com.stec.srms.activity.OnboardingActivity;
import com.stec.srms.activity.StudentInfoActivity;

public class LoginStudentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_student, container, false);
        Context context = requireContext();
        EditText loginStudentIdInput, loginStudentPwInput;
        AppCompatButton loginStudentButton, loginStudentForgetPwButton, loginStudentCreateAccountButton;
        loginStudentIdInput = view.findViewById(R.id.loginStudentIdInput);
        loginStudentPwInput = view.findViewById(R.id.loginStudentPwInput);
        loginStudentButton = view.findViewById(R.id.loginStudentButton);
        loginStudentForgetPwButton = view.findViewById(R.id.loginStudentForgetPwButton);
        loginStudentCreateAccountButton = view.findViewById(R.id.loginStudentCreateAccountButton);
        loginStudentButton.setOnClickListener(v -> {
            // All checkings
            startActivity(new Intent(context, StudentInfoActivity.class));
            requireActivity().finish();
        });

        makePasswordShowable(loginStudentPwInput, context);
        return view;
    }
}