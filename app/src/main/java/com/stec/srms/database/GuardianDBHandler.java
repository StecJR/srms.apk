package com.stec.srms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.model.PendingGuardian;
import com.stec.srms.util.Toast;

public class GuardianDBHandler extends Database {
    private static GuardianDBHandler guardianDBHandlerInstance;

    public GuardianDBHandler(Context context) {
        super(context);
    }

    public static synchronized GuardianDBHandler getInstance(Context context) {
        if (guardianDBHandlerInstance == null)
            guardianDBHandlerInstance = new GuardianDBHandler(context.getApplicationContext());
        return guardianDBHandlerInstance;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM guardians WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                db.close();
                cursor.close();
                return true;
            }

            query = "SELECT * FROM pending_guardians WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean isStudentExists(int deptId, int studentId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public int addPendingGuardian(Context context, PendingGuardian guardianInfo) {
        int accountId = getAccountType("pendingGuardian").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("name", guardianInfo.name);
            values.put("relation", guardianInfo.relation);
            values.put("contact", guardianInfo.contact);
            values.put("email", guardianInfo.email);
            values.put("studentId", guardianInfo.studentId);
            values.put("deptId", guardianInfo.deptId);
            values.put("password", guardianInfo.password);
            int userId = (int) db.insert("pending_guardians", null, values);
            if (userId == -1) {
                Toast.databaseError(context, "Failed to add guardian record");
                return -1;
            }

            values = new ContentValues();
            values.put("accountId", accountId);
            values.put("userId", userId);
            int verificationId = (int) db.insert("pending_verifications", null, values);
            if (verificationId == -1) {
                Toast.databaseError(context, "Failed to add guardian record");
                return -1;
            }

            db.setTransactionSuccessful();
            return userId;
        } catch (Exception e) {
            return -1;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
