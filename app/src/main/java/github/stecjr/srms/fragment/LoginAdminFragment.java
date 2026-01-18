package github.stecjr.srms.fragment;

import static github.stecjr.srms.util.Util.togglePasswordVisibility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import github.stecjr.srms.R;
import github.stecjr.srms.activity.AdminDashboardActivity;
import github.stecjr.srms.database.AdminDBHandler;
import github.stecjr.srms.util.SessionManager;
import github.stecjr.srms.util.Toast;

public class LoginAdminFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);
        Context context = requireContext();
        FragmentActivity activity = requireActivity();
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(context);
        EditText loginAdminIdInput, loginAdminPwInput;
        AppCompatButton loginAdminLoginButton;

        loginAdminIdInput = view.findViewById(R.id.loginAdminIdInput);
        loginAdminPwInput = view.findViewById(R.id.loginAdminPwInput);
        loginAdminLoginButton = view.findViewById(R.id.loginAdminLoginButton);

        loginAdminLoginButton.setOnClickListener(v -> {
            String adminName = loginAdminIdInput.getText().toString().trim();
            String adminPw = loginAdminPwInput.getText().toString().trim();
            boolean isValid = !adminName.isBlank() &&
                    !adminPw.isBlank() &&
                    adminDBHandler.isValidAdmin(adminName, adminPw);
            if (!isValid) {
                Toast.credentialError(context);
                return;
            }

            SessionManager sessionManager = SessionManager.getInstance(context);
            sessionManager.createAdminSession(15);
            startActivity(new Intent(context, AdminDashboardActivity.class));
            activity.finish();
        });

        togglePasswordVisibility(context, loginAdminPwInput);
        return view;
    }
}