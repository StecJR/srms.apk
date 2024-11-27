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

public class LoginTeacherFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_teacher, container, false);
        Context context = requireContext();
        EditText loginTeacherIdInput, loginTeacherPwInput;
        loginTeacherIdInput = view.findViewById(R.id.loginTeacherIdInput);
        loginTeacherPwInput = view.findViewById(R.id.loginTeacherPwInput);

        makePasswordShowable(loginTeacherPwInput, context);

        return view;
    }
}