package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class AdminPendingFacultyActivity extends AppCompatActivity {
    AdminDBHandler adminDBHandler;
    TableLayout adminPendingFacultyTable;
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createTableRows() {
        ArrayList<PendingUserInfo> pendingUsers = adminDBHandler.getPendingFaculties();
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
                Intent intent = new Intent(this, AdminPendingFacultyInfoActivity.class);
                intent.putExtra("pendingFacultyId", pendingUser.userId);
                startActivity(intent);
            });
            adminPendingFacultyTable.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_faculty);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        adminDBHandler = AdminDBHandler.getInstance(this);
        adminPendingFacultyTable = findViewById(R.id.adminPendingFacultyTable);
        createTableRows();
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = adminPendingFacultyTable.getChildCount() - 1; i > 0; i--)
            adminPendingFacultyTable.removeViewAt(i);
        createTableRows();
    }
}