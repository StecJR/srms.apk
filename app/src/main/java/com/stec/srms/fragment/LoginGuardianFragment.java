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
import com.stec.srms.activity.GuardianInfoActivity;
import com.stec.srms.activity.StudentInfoActivity;

public class LoginGuardianFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_guardian, container, false);
        Context context = requireContext();
        EditText loginGuardiantIdInput, loginGuardianPwInput;
        AppCompatButton loginGuardianButton, loginGuardianForgetPwButton, loginGuardianCreateAccountButton;

        loginGuardiantIdInput = view.findViewById(R.id.loginGuardianIdInput);
        loginGuardianPwInput = view.findViewById(R.id.loginGuardianPwInput);
        loginGuardianButton = view.findViewById(R.id.loginGuardianButton);
        loginGuardianForgetPwButton = view.findViewById(R.id.loginGuardianForgetPwButton);
        loginGuardianCreateAccountButton = view.findViewById(R.id.loginGuardianCreateAccountButton);

        loginGuardianButton.setOnClickListener(v -> {
            // All checkings
            startActivity(new Intent(context, GuardianInfoActivity.class));
            requireActivity().finish();
        });

        makePasswordShowable(loginGuardianPwInput, context);

        return view;
    }
}