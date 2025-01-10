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
import com.stec.srms.activity.FacultyInfoActivity;
import com.stec.srms.activity.FacultySignupActivity;
import com.stec.srms.activity.StudentSignupActivity;

public class LoginFacultyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_faculty, container, false);
        Context context = requireContext();
        EditText loginFacultyIdInput, loginFacultyPwInput;
        AppCompatButton loginFacultyLoginButton, loginFacultyForgetPwButton, loginFacultyCreateAccountButton;

        loginFacultyIdInput = view.findViewById(R.id.loginFacultyIdInput);
        loginFacultyPwInput = view.findViewById(R.id.loginFacultyPwInput);
        loginFacultyLoginButton = view.findViewById(R.id.loginFacultyButton);
        loginFacultyForgetPwButton = view.findViewById(R.id.loginFacultyForgetPwButton);
        loginFacultyCreateAccountButton = view.findViewById(R.id.loginFacultyCreateAccountButton);

        loginFacultyLoginButton.setOnClickListener(v -> {
            // All checkings
            startActivity(new Intent(context, FacultyInfoActivity.class));
            requireActivity().finish();
        });
        loginFacultyCreateAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(context, FacultySignupActivity.class));
        });

        makePasswordShowable(loginFacultyPwInput, context);
        return view;
    }
}