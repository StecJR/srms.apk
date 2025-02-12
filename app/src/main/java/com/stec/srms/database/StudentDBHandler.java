package com.stec.srms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.model.GuardianInfo;
import com.stec.srms.model.PendingStudent;
import com.stec.srms.model.Results;
import com.stec.srms.model.ResultsSummary;
import com.stec.srms.model.StudentInfo;
import com.stec.srms.util.Toast;

import java.util.ArrayList;

public class StudentDBHandler extends Database {
    private static StudentDBHandler studentDBHandlerInstance;

    public StudentDBHandler(Context context) {
        super(context);
    }

    public static synchronized StudentDBHandler getInstance(Context context) {
        if (studentDBHandlerInstance == null)
            studentDBHandlerInstance = new StudentDBHandler(context.getApplicationContext());
        return studentDBHandlerInstance;
    }

    public boolean isEmailExists(int deptId, String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM students_" + deptId + " WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                db.close();
                cursor.close();
                return true;
            }

            query = "SELECT * FROM pending_students WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public ArrayList<StudentInfo> getStudents(int deptId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM students_" + deptId + ";";
            cursor = db.rawQuery(query, null);
            ArrayList<StudentInfo> students = null;
            if (cursor.moveToFirst()) {
                students = new ArrayList<>();
                StudentInfo info;
                do {
                    info = new StudentInfo();
                    info.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    info.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    info.birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate"));
                    info.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                    info.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    info.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                    info.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                    info.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    info.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    info.guardianId = cursor.getInt(cursor.getColumnIndexOrThrow("guardianId"));
                    info.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                    students.add(info);
                } while (cursor.moveToNext());
            }
            return students;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public ArrayList<StudentInfo> getStudents(int deptId, int sessionId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM students_" + deptId + " WHERE sessionId = " + sessionId + ";";
            cursor = db.rawQuery(query, null);
            ArrayList<StudentInfo> students = null;
            if (cursor.moveToFirst()) {
                students = new ArrayList<>();
                StudentInfo info;
                do {
                    info = new StudentInfo();
                    info.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    info.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    info.birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate"));
                    info.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                    info.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    info.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                    info.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                    info.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    info.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    info.guardianId = cursor.getInt(cursor.getColumnIndexOrThrow("guardianId"));
                    info.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                    students.add(info);
                } while (cursor.moveToNext());
            }
            return students;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public StudentInfo getStudentinfo(int deptId, int studentId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            StudentInfo info = null;
            if (cursor.moveToFirst()) {
                info = new StudentInfo();
                info.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                info.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                info.birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate"));
                info.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                info.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                info.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                info.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                info.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                info.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                info.guardianId = cursor.getInt(cursor.getColumnIndexOrThrow("guardianId"));
                info.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            return info;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public GuardianInfo getGuardianinfo(int guardianId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM guardians WHERE guardianId = " + guardianId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            GuardianInfo info = null;
            if (cursor.moveToFirst()) {
                info = new GuardianInfo();
                info.guardianId = cursor.getInt(cursor.getColumnIndexOrThrow("guardianId"));
                info.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                info.relation = cursor.getString(cursor.getColumnIndexOrThrow("relation"));
                info.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                info.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                info.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                info.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                info.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            }
            return info;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public ArrayList<Results> getResults(int sessionId, int deptId, int studentId, int semesterId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM results_" + sessionId + "_" + deptId + " WHERE studentId = " + studentId + " AND semesterId = " + semesterId + " ORDER BY courseCode;";
            cursor = db.rawQuery(query, null);
            ArrayList<Results> results = null;
            if (cursor.moveToFirst()) {
                results = new ArrayList<>();
                Results result;
                do {
                    result = new Results();
                    result.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    result.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    result.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    result.mark = cursor.getInt(cursor.getColumnIndexOrThrow("mark"));
                    result.gpa = cursor.getDouble(cursor.getColumnIndexOrThrow("gpa"));
                    results.add(result);
                } while (cursor.moveToNext());
            }
            return results;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public ArrayList<ResultsSummary> getResultsSummaries(int sessionId, int deptId, int studentId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM results_summary_" + sessionId + "_" + deptId + " WHERE studentId = " + studentId + " ORDER BY semesterId;";
            cursor = db.rawQuery(query, null);
            ArrayList<ResultsSummary> summaries = null;
            if (cursor.moveToFirst()) {
                summaries = new ArrayList<>();
                ResultsSummary summary;
                do {
                    summary = new ResultsSummary();
                    summary.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    summary.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    summary.gpa = cursor.getDouble(cursor.getColumnIndexOrThrow("gpa"));
                    summaries.add(summary);
                } while (cursor.moveToNext());
            }
            return summaries;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

    }

    public int addPendingStudent(Context context, PendingStudent studentInfo) {
        int accountId = getAccountType("pendingStudent").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

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
            int userId = (int) db.insert("pending_students", null, values);
            if (userId == -1) {
                Toast.databaseError(context, "Failed to add student record");
                return -1;
            }

            values = new ContentValues();
            values.put("accountId", accountId);
            values.put("userId", userId);
            int verificationId = (int) db.insert("pending_verifications", null, values);
            if (verificationId == -1) {
                Toast.databaseError(context, "Failed to add student record");
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
