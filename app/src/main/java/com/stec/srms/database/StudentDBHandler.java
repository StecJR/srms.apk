package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.model.GuardianInfo;
import com.stec.srms.model.Results;
import com.stec.srms.model.ResultsSummary;
import com.stec.srms.model.StudentInfo;

import java.util.ArrayList;

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

    public ArrayList<StudentInfo> getStudents(int deptId) {
        ArrayList<StudentInfo> students = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        StudentInfo info = null;
        try {
            String query = "SELECT * FROM students_" + deptId + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return students;
    }

    public ArrayList<StudentInfo> getStudents(int deptId, int sessionId) {
        ArrayList<StudentInfo> students = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        StudentInfo info = null;
        try {
            String query = "SELECT * FROM students_" + deptId + " WHERE sessionId = " + sessionId + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return students;
    }

    public StudentInfo getStudentinfo(int deptId, int studentId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        StudentInfo info = null;
        try {
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId + " LIMIT 1;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return info;
    }

    public GuardianInfo getGuardianinfo(int guardianId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        GuardianInfo info = null;
        try {
            String query = "SELECT * FROM guardians WHERE guardianId = " + guardianId + " LIMIT 1;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return info;
    }

    public ArrayList<ResultsSummary> getResultsSummaries(int sessionId, int deptId, int studentId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ResultsSummary> summaries = null;
        try {
            String query = "SELECT * FROM results_summary_" + sessionId + "_" + deptId + " WHERE studentId = " + studentId + " ORDER BY semesterId;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                summaries = new ArrayList<>();
                do {
                    ResultsSummary summary = new ResultsSummary();
                    summary.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    summary.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    summary.gpa = cursor.getDouble(cursor.getColumnIndexOrThrow("gpa"));
                    summaries.add(summary);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return summaries;
    }

    public ArrayList<Results> getResults(int sessionId, int deptId, int studentId, int semesterId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<Results> results = null;
        try {
            String query = "SELECT * FROM results_" + sessionId + "_" + deptId + " WHERE studentId = " + studentId + " AND semesterId = " + semesterId + " ORDER BY courseCode;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                results = new ArrayList<>();
                do {
                    Results result = new Results();
                    result.studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
                    result.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    result.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    result.mark = cursor.getInt(cursor.getColumnIndexOrThrow("mark"));
                    result.gpa = cursor.getDouble(cursor.getColumnIndexOrThrow("gpa"));
                    results.add(result);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return results;
    }
}
