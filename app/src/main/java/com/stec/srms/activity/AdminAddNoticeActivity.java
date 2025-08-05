package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.stec.srms.R;
import com.stec.srms.adapter.AccountTypeSelectorAdapter;
import com.stec.srms.database.AdminDBHandler;
import com.stec.srms.model.AccountType;
import com.stec.srms.model.Notice;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AdminAddNoticeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_notice);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activeMenuIndex", 3);
            startActivity(intent);
            finish();
            return;
        } else if (!accountType.equals("admin")) {
            switch (accountType) {
                case "student":
                    startActivity(new Intent(this, StudentInfoActivity.class));
                    break;
                case "faculty":
                    startActivity(new Intent(this, FacultyInfoActivity.class));
                    break;
                case "guardian":
                    startActivity(new Intent(this, GuardianInfoActivity.class));
                    break;
                default:
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("activeMenuIndex", 3);
                    startActivity(intent);
            }
            finish();
            return;
        }

        Spinner targetUserSpinner;
        TextInputEditText noticeTitleInput, noticeBodyInput;
        AppCompatButton noticeSaveButton;

        targetUserSpinner = findViewById(R.id.targetUserSpinner);
        noticeTitleInput = findViewById(R.id.noticeTitleInput);
        noticeBodyInput = findViewById(R.id.noticeBodyInput);
        noticeSaveButton = findViewById(R.id.noticeSaveButton);

        ArrayList<AccountType> accountTypes = new ArrayList<>();
        accountTypes.add(new AccountType(adminDBHandler.getAccountType("student").accountId, "Student"));
        accountTypes.add(new AccountType(adminDBHandler.getAccountType("faculty").accountId, "Faculty"));
        accountTypes.add(new AccountType(adminDBHandler.getAccountType("guardian").accountId, "Guardian"));
        accountTypes.add(new AccountType(Integer.parseInt("" + accountTypes.get(0).accountId + accountTypes.get(1).accountId), "Student & Faculty"));
        accountTypes.add(new AccountType(Integer.parseInt("" + accountTypes.get(0).accountId + accountTypes.get(2).accountId), "Student & Guardian"));
        accountTypes.add(new AccountType(Integer.parseInt("" + accountTypes.get(1).accountId + accountTypes.get(2).accountId), "Faculty & Guardian"));
        accountTypes.add(new AccountType(11111111, "All"));
        if (accountTypes.get(0).accountId != -1) {
            accountTypes.add(0, new AccountType(-1, "Target User"));
        }
        AccountTypeSelectorAdapter accountTypeSelectorAdapter = new AccountTypeSelectorAdapter(this, accountTypes);
        targetUserSpinner.setAdapter(accountTypeSelectorAdapter);
        targetUserSpinner.setSelection(0);

        noticeSaveButton.setOnClickListener(v -> {
            int accountId = (int) targetUserSpinner.getSelectedItemId();
            if (accountId == -1) {
                Toast.generalError(this, "Select target user");
                return;
            }
            String noticeTitle = Objects.requireNonNull(noticeTitleInput.getText()).toString();
            String noticeBody = Objects.requireNonNull(noticeBodyInput.getText()).toString();
            if (noticeTitle.isBlank() || noticeBody.isBlank()) {
                Toast.generalError(this, "Please fill in all fields");
                return;
            }
            try {
                adminDBHandler.addNotice(new Notice(0, accountId, noticeTitle, noticeBody, ""));
                Toast.generalSuccess(this, "Notice successfully added");
                targetUserSpinner.setSelection(0);
            } catch (Exception e) {
                Toast.generalError(this, "Failed to add notice");
            }
        });
    }
}