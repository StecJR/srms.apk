package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.util.Toast;

public class AdminDBHandler extends Database {
    private static AdminDBHandler adminDBHandlerInstance;

    public AdminDBHandler(Context context) {
        super(context);
    }

    public static synchronized AdminDBHandler getInstance(Context context) {
        if (adminDBHandlerInstance == null)
            adminDBHandlerInstance = new AdminDBHandler(context.getApplicationContext());
        return adminDBHandlerInstance;
    }

    public boolean isValidAdmin(Context context, String adminName, String adminPw) {
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM admin_info WHERE adminName = '" + adminName + "' AND adminPw = '" + adminPw + "' LIMIT 1";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            isValid = cursor.moveToFirst();
        } catch (Exception e) {
            Toast.databaseError(context, e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return isValid;
    }
}

