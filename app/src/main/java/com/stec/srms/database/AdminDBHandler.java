package com.stec.srms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.model.PendingFaculty;
import com.stec.srms.model.PendingGuardian;
import com.stec.srms.model.PendingStudent;
import com.stec.srms.model.PendingUserInfo;
import com.stec.srms.util.Toast;

import java.util.ArrayList;

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

    public ArrayList<PendingUserInfo> getPendingStudents() {
        ArrayList<PendingUserInfo> pendingUsers = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            int accountId = getAccountType("pendingStudent").accountId;
            String query = "SELECT pv.userId, di.shortDesc, ps.email " +
                    "FROM pending_verifications pv " +
                    "JOIN pending_students ps ON pv.userId = ps.userId " +
                    "JOIN dept_info di ON ps.deptId = di.deptId " +
                    "WHERE pv.accountId = " + accountId + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                pendingUsers = new ArrayList<>();
                do {
                    PendingUserInfo pendingUser = new PendingUserInfo();
                    pendingUser.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                    pendingUser.shortDept = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    pendingUser.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    pendingUsers.add(pendingUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return pendingUsers;
    }

    public ArrayList<PendingUserInfo> getPendingFaculties() {
        ArrayList<PendingUserInfo> pendingUsers = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            int accountId = getAccountType("pendingFaculty").accountId;
            String query = "SELECT pv.userId, di.shortDesc, pf.email " +
                    "FROM pending_verifications pv " +
                    "JOIN pending_faculties pf ON pv.userId = pf.userId " +
                    "JOIN dept_info di ON pf.deptId = di.deptId " +
                    "WHERE pv.accountId = " + accountId + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                pendingUsers = new ArrayList<>();
                do {
                    PendingUserInfo pendingUser = new PendingUserInfo();
                    pendingUser.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                    pendingUser.shortDept = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    pendingUser.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    pendingUsers.add(pendingUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return pendingUsers;
    }

    public ArrayList<PendingUserInfo> getPendingGuardians() {
        ArrayList<PendingUserInfo> pendingUsers = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            int accountId = getAccountType("pendingGuardian").accountId;
            String query = "SELECT pv.userId, di.shortDesc, pg.email " +
                    "FROM pending_verifications pv " +
                    "JOIN pending_guardians pg ON pv.userId = pg.userId " +
                    "JOIN dept_info di ON pg.deptId = di.deptId " +
                    "WHERE pv.accountId = " + accountId + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                pendingUsers = new ArrayList<>();
                do {
                    PendingUserInfo pendingUser = new PendingUserInfo();
                    pendingUser.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                    pendingUser.shortDept = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    pendingUser.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    pendingUsers.add(pendingUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return pendingUsers;
    }

    public PendingStudent getPendingStudent(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM pending_students WHERE userId = " + userId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            PendingStudent pendingStudent = null;
            if (cursor.moveToFirst()) {
                pendingStudent = new PendingStudent();
                pendingStudent.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                pendingStudent.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                pendingStudent.birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate"));
                pendingStudent.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                pendingStudent.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                pendingStudent.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                pendingStudent.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                pendingStudent.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                pendingStudent.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                pendingStudent.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            return pendingStudent;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public PendingFaculty getPendingFaculty(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM pending_faculties WHERE userId = " + userId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            PendingFaculty pendingFaculty = null;
            if (cursor.moveToFirst()) {
                pendingFaculty = new PendingFaculty();
                pendingFaculty.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                pendingFaculty.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                pendingFaculty.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                pendingFaculty.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                pendingFaculty.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                pendingFaculty.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                pendingFaculty.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                pendingFaculty.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            return pendingFaculty;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public PendingGuardian getPendingGuardian(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM pending_students WHERE userId = " + userId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            PendingGuardian pendingGuardian = null;
            if (cursor.moveToFirst()) {
                pendingGuardian = new PendingGuardian();
                pendingGuardian.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                pendingGuardian.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                pendingGuardian.relation = cursor.getString(cursor.getColumnIndexOrThrow("relation"));
                pendingGuardian.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                pendingGuardian.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                pendingGuardian.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                pendingGuardian.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                pendingGuardian.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            return pendingGuardian;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean deletePendingStudent(int userId) {
        int accountId = getAccountType("pendingStudent").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            db.delete("pending_students", "userId = " + userId, null);
            db.delete("pending_verifications", "accountId = " + accountId + " AND userId = " + userId, null);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public boolean deletePendingFaculty(int userId) {
        int accountId = getAccountType("pendingFaculty").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            db.delete("pending_faculties", "userId = " + userId, null);
            db.delete("pending_verifications", "accountId = " + accountId + " AND userId = " + userId, null);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public boolean deletePendingGuardian(int userId) {
        int accountId = getAccountType("pendingGuardian").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            db.delete("pending_guardians", "userId = " + userId, null);
            db.delete("pending_verifications", "accountId = " + accountId + " AND userId = " + userId, null);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public int movePendingStudentToStudentTable(PendingStudent studentInfo) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("name", studentInfo.name);
            values.put("birthDate", studentInfo.birthDate);
            values.put("gender", studentInfo.gender);
            values.put("deptId", studentInfo.deptId);
            values.put("sessionId", studentInfo.sessionId);
            values.put("contact", studentInfo.contact);
            values.put("email", studentInfo.email);
            values.put("address", studentInfo.address);
            values.put("password", studentInfo.password);
            return (int) db.insert("students_" + studentInfo.deptId, null, values);
        } catch (Exception e) {
            return -1;
        }
    }
}

