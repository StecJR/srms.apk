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

public class LoginFacultyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_faculty, container, false);
        Context context = requireContext();
        EditText loginTeacherIdInput, loginTeacherPwInput;
        AppCompatButton loginTeacherButton, loginTeacherForgetPwButton, loginTeacherCreateAccountButton;

        loginTeacherIdInput = view.findViewById(R.id.loginTeacherIdInput);
        loginTeacherPwInput = view.findViewById(R.id.loginTeacherPwInput);
        loginTeacherButton = view.findViewById(R.id.loginTeacherButton);
        loginTeacherForgetPwButton = view.findViewById(R.id.loginTeacherForgetPwButton);
        loginTeacherCreateAccountButton = view.findViewById(R.id.loginTeacherCreateAccountButton);

        loginTeacherButton.setOnClickListener(v -> {
            // All checkings
            startActivity(new Intent(context, FacultyInfoActivity.class));
            requireActivity().finish();
        });

        makePasswordShowable(loginTeacherPwInput, context);

        return view;
    }
}