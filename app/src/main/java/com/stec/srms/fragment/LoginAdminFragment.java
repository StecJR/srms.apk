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

public class LoginAdminFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);
        Context context = requireContext();
        EditText loginAdminIdInput, loginAdminPwInput;
        loginAdminIdInput = view.findViewById(R.id.loginAdminIdInput);
        loginAdminPwInput = view.findViewById(R.id.loginAdminPwInput);

        makePasswordShowable(loginAdminPwInput, context);

        return view;
    }
}