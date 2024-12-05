package com.stec.srms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.stec.srms.R;

public class StudentResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_result);

        TableLayout table = findViewById(R.id.table);
        table.setOnClickListener(view -> {
            startActivity(new Intent(this, StudentMarksheetActivity.class));
        });
    }
}