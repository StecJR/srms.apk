package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.stec.srms.R;
import com.stec.srms.database.AdminDBHandler;
import com.stec.srms.model.PendingUserInfo;

import java.util.ArrayList;

public class AdminPendingGuardianActivity extends AppCompatActivity {
    private AdminDBHandler adminDBHandler;
    private TableLayout adminPendingGuardianTable;
    private ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createTableRows() {
        ArrayList<PendingUserInfo> pendingUsers = adminDBHandler.getPendingGuardians();
        if (pendingUsers == null || pendingUsers.isEmpty()) return;

        TableRow tableRow;
        TableRow.LayoutParams weightParam;
        TextView deptTextView, emailTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        for (PendingUserInfo pendingUser : pendingUsers) {
            tableRow = new TableRow(tableRowStyle);
            tableRow.setPadding(tableRow.getPaddingLeft(), 10, tableRow.getPaddingRight(), 10);

            deptTextView = new TextView(tableRowTextStyle);
            deptTextView.setText(pendingUser.shortDept);
            deptTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
            tableRow.addView(deptTextView);

            emailTextView = new TextView(tableRowTextStyle);
            emailTextView.setLayoutParams(weightParam);
            emailTextView.setText(pendingUser.email);
            emailTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
            tableRow.addView(emailTextView);

            tableRow.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminPendingGuardianInfoActivity.class);
                intent.putExtra("pendingGuardianId", pendingUser.userId);
                Log.e("TAG", "createTableRows: " + pendingUser.userId);
                startActivity(intent);
            });
            adminPendingGuardianTable.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_guardian);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        adminDBHandler = AdminDBHandler.getInstance(this);
        adminPendingGuardianTable = findViewById(R.id.adminPendingGuardianTable);
        createTableRows();
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = adminPendingGuardianTable.getChildCount() - 1; i > 0; i--)
            adminPendingGuardianTable.removeViewAt(i);
        createTableRows();
    }
}