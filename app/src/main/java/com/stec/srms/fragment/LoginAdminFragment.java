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
import com.stec.srms.activity.AdminDashboardActivity;

public class LoginAdminFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);
        Context context = requireContext();
        EditText loginAdminIdInput, loginAdminPwInput;
        AppCompatButton loginAdminButton;

        loginAdminIdInput = view.findViewById(R.id.loginAdminIdInput);
        loginAdminPwInput = view.findViewById(R.id.loginAdminPwInput);
        loginAdminButton = view.findViewById(R.id.loginAdminButton);

        loginAdminButton.setOnClickListener(v -> {
            // All checkings
            startActivity(new Intent(context, AdminDashboardActivity.class));
            requireActivity().finish();
        });

        makePasswordShowable(loginAdminPwInput, context);

        return view;
    }
}