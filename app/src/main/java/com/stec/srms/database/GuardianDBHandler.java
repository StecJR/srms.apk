package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GuardianDBHandler extends Database {
    private static GuardianDBHandler guardianDBHandlerInstance;

    public GuardianDBHandler(Context context) {
        super(context);
    }
    public static synchronized GuardianDBHandler getInstance(Context context) {
        if (guardianDBHandlerInstance == null) {
            guardianDBHandlerInstance = new GuardianDBHandler(context.getApplicationContext());
        }
        return guardianDBHandlerInstance;
    }

    public boolean isValidGuardian(String guardianId, String guardianPw) {
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM guardians WHERE guardianId = " + guardianId +" AND password = '" + guardianPw + "' LIMIT 1";
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
