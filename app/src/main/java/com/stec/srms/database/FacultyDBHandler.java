package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FacultyDBHandler extends Database {
    private static FacultyDBHandler facultyDBHandlerInstance;

    public FacultyDBHandler(Context context) {
        super(context);
    }

    public static synchronized FacultyDBHandler getInstance(Context context) {
        if (facultyDBHandlerInstance == null) {
            facultyDBHandlerInstance = new FacultyDBHandler(context.getApplicationContext());
        }
        return facultyDBHandlerInstance;
    }

    public boolean isValidFaculty(String facultyId, String facultyPw) {
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM faculties WHERE facultyId = " + facultyId + " AND password = '" + facultyPw + "' LIMIT 1";
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

