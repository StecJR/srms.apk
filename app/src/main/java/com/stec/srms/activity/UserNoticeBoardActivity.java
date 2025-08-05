package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.stec.srms.R;
import com.stec.srms.database.Database;
import com.stec.srms.model.Notice;
import com.stec.srms.util.SessionManager;

import java.util.ArrayList;

public class UserNoticeBoardActivity extends AppCompatActivity {
    private ContextThemeWrapper tableRowStyle, tableRowTextStyle;
    private Database database;
    private TableLayout tableLayout;
    private String sqlCondition;

    private void createNoticeBoardTable(TableLayout tableLayout, ArrayList<Notice> notices) {
        TableRow tableRow;
        TableRow.LayoutParams weightParam;
        TextView dateTextView, titleTextView;

        weightParam = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        for (Notice notice : notices) {
            tableRow = new TableRow(tableRowStyle);

            dateTextView = new TextView(tableRowTextStyle);
            String createdAt = notice.createdAt;
            dateTextView.setText(createdAt);
            dateTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
            tableRow.addView(dateTextView);

            titleTextView = new TextView(tableRowTextStyle);
            titleTextView.setLayoutParams(weightParam);
            titleTextView.setText(notice.title);
            titleTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(titleTextView);

            tableRow.setOnClickListener(view -> {
                Intent intent = new Intent(this, UserShowNoticeActivity.class);
                intent.putExtra("title", notice.title);
                intent.putExtra("description", notice.description);
                intent.putExtra("createdAt", createdAt);
                startActivity(intent);
            });
            tableLayout.addView(tableRow);
        }
    }

    private void clearTable(TableLayout tableLayout) {
        for (int i = tableLayout.getChildCount() - 1; i > 0; i--) tableLayout.removeViewAt(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_notice_board);
        tableRowStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRow);
        tableRowTextStyle = new ContextThemeWrapper(this, R.style.CustomStyle_Table_Simple_SimpleRowText);
        database = Database.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Verify or goto login page
        String accountType = sessionManager.validSession();
        if (accountType == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        tableLayout = findViewById(R.id.userNoticeBoardTable);
        LinearLayout userNoticeBoardButtonLayout = findViewById(R.id.userNoticeBoardButtonLayout);
        AppCompatButton userNoticeBoardAddButton, userNoticeBoardDeleteButton;
        sqlCondition = "1 = 1";
        if (!accountType.equals("admin")) {
            userNoticeBoardButtonLayout.setVisibility(LinearLayout.GONE);
            String studentId = String.valueOf(database.getAccountType("student").accountId);
            String facultyId = String.valueOf(database.getAccountType("faculty").accountId);
            String guardianId = String.valueOf(database.getAccountType("guardian").accountId);
            switch (accountType) {
                case "student":
                    sqlCondition = "targetUserId = " + studentId + " OR targetUserId = " + studentId + facultyId + " OR targetUserId = " + studentId + guardianId + " OR targetUserId = 11111111";
                    break;
                case "faculty":
                    sqlCondition = "targetUserId = " + facultyId + " OR targetUserId = " + studentId + facultyId + " OR targetUserId = " + facultyId + guardianId + " OR targetUserId = 11111111";
                    break;
                case "guardian":
                    sqlCondition = "targetUserId = " + guardianId + " OR targetUserId = " + studentId + guardianId + " OR targetUserId = " + facultyId + guardianId + " OR targetUserId = 11111111";
                    break;
            }
        }

        ArrayList<Notice> notices = database.getNotices(1, 15, sqlCondition);
        if (notices != null && !notices.isEmpty()) {
            createNoticeBoardTable(tableLayout, notices);
        }

        if (accountType.equals("admin")) {
            userNoticeBoardAddButton = findViewById(R.id.userNoticeBoardAddButton);
            userNoticeBoardDeleteButton = findViewById(R.id.userNoticeBoardDeleteButton);
            userNoticeBoardAddButton.setOnClickListener(v -> startActivity(new Intent(this, AdminAddNoticeActivity.class)));
            userNoticeBoardDeleteButton.setOnClickListener(v -> startActivity(new Intent(this, AdminDeleteNoticeActivity.class)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearTable(tableLayout);
        ArrayList<Notice> notices = database.getNotices(1, 15, sqlCondition);
        if (notices != null && !notices.isEmpty()) {
            createNoticeBoardTable(tableLayout, notices);
        }
    }
}