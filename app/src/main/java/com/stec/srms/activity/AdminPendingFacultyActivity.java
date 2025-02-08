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
    ContextThemeWrapper tableRowStyle, tableRowTextStyle;

    private void createTableRows(TableLayout tableLayout, ArrayList<PendingUserInfo> pendingUsers) {
        if (pendingUsers == null || pendingUsers.isEmpty()) return;

        TableRow tableRow;
        TableRow.LayoutParams weightParam;
        TextView deptTextView, emailTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        for (PendingUserInfo pendingUser : pendingUsers) {
            tableRow = new TableRow(tableRowStyle);
            tableRow.setPadding(tableRow.getPaddingLeft(), 5, tableRow.getPaddingRight(), 5);

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
                intent.putExtra("pendingStudentId", pendingUser.userId);
                startActivity(intent);
            });
            tableLayout.addView(tableRow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_faculty);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        AdminDBHandler adminDBHandler = AdminDBHandler.getInstance(this);

        TableLayout adminPendingFacultyTable = findViewById(R.id.adminPendingFacultyTable);
        createTableRows(adminPendingFacultyTable, adminDBHandler.getPendingFaculties());
    }
}