package com.stec.srms.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.stec.srms.R;
import com.stec.srms.adapter.OptionSelectorAdapter;
import com.stec.srms.database.Database;
import com.stec.srms.model.Notice;
import com.stec.srms.model.Option;
import com.stec.srms.util.SessionManager;
import com.stec.srms.util.Toast;

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

    private void openDeleteNoticeDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_delete_notice, null);
        dialog.setContentView(view);

        Spinner optionSpinner = view.findViewById(R.id.optionSpinner);
        AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
        AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(-1, "Deletion Group"));
        options.add(new Option(0, "Last added notice"));
        options.add(new Option(1, "Older than 6 months"));
        options.add(new Option(2, "Older than 1 year"));
        options.add(new Option(3, "Older than 2 years"));
        options.add(new Option(4, "Older than 3 years"));
        OptionSelectorAdapter adapter = new OptionSelectorAdapter(this, options);
        optionSpinner.setAdapter(adapter);
        optionSpinner.setSelection(0);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        deleteButton.setOnClickListener(v -> {
            boolean isDismissed = true;
            switch ((int) optionSpinner.getSelectedItemId()) {
                case -1:
                    Toast.generalError(this, "Select an option");
                    return;
                case 0:
                    database.deleteLastNotice();
                    optionSpinner.setSelection(0);
                    isDismissed = false;
                    break;
                case 1:
                    database.deleteNoticeOlderThan(6);
                    break;
                case 2:
                    database.deleteNoticeOlderThan(12);
                    break;
                case 3:
                    database.deleteNoticeOlderThan(24);
                    break;
                case 4:
                    database.deleteNoticeOlderThan(36);
                    break;
                default:
                    Toast.generalError(this, "Select a valid option");
                    break;
            }

            Toast.generalSuccess(this, "Notices deleted");
            clearTable(tableLayout);
            ArrayList<Notice> notices = database.getNotices(1, 15, sqlCondition);
            if (notices != null && !notices.isEmpty()) {
                createNoticeBoardTable(tableLayout, notices);
            }
            if (isDismissed) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
            userNoticeBoardDeleteButton.setOnClickListener(v -> openDeleteNoticeDialog());
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