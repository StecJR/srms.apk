package com.stec.srms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StudentDBHandler extends Database {
    private static StudentDBHandler studentDBHandlerInstance;

    public StudentDBHandler(Context context) {
        super(context);
    }
    public static synchronized StudentDBHandler getInstance(Context context) {
        if (studentDBHandlerInstance == null) {
            studentDBHandlerInstance = new StudentDBHandler(context.getApplicationContext());
        }
        return studentDBHandlerInstance;
    }
}
