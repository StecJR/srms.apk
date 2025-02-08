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

public class AdminPendingStudentActivity extends AppCompatActivity {
    AdminDBHandler adminDBHandler;
    TableLayout adminPendingStudentTable;
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createTableRows() {
        ArrayList<PendingUserInfo> pendingUsers = adminDBHandler.getPendingStudents();
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
                Intent intent = new Intent(this, AdminPendingStudentInfoActivity.class);
                intent.putExtra("pendingStudentId", pendingUser.userId);
                startActivity(intent);
            });
            adminPendingStudentTable.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_student);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        adminDBHandler = AdminDBHandler.getInstance(this);
        adminPendingStudentTable = findViewById(R.id.adminPendingStudentTable);
        createTableRows();
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = adminPendingStudentTable.getChildCount() - 1; i > 0; i--)
            adminPendingStudentTable.removeViewAt(i);
        createTableRows();
    }
}