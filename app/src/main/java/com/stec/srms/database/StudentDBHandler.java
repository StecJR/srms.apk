package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
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

    public boolean isValidStudent(int deptId, String studentId, String studentPw) {
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId +" AND password = '" + studentPw + "' LIMIT 1";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) isValid = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return isValid;
    }
}
