package com.stec.srms.fragment;

import static com.stec.srms.util.Util.makePasswordShowable;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stec.srms.R;

public class LoginGuardianFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_guardian, container, false);
        Context context = requireContext();
        EditText loginGuardiantIdInput, loginGuardianPwInput;
        loginGuardiantIdInput = view.findViewById(R.id.loginGuardianIdInput);
        loginGuardianPwInput = view.findViewById(R.id.loginGuardianPwInput);

        makePasswordShowable(loginGuardianPwInput, context);

        return view;
    }
}