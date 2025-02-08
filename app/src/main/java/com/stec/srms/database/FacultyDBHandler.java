package com.stec.srms.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stec.srms.model.CourseInfo;
import com.stec.srms.model.FacultyInfo;
import com.stec.srms.model.PendingFaculty;
import com.stec.srms.model.Results;
import com.stec.srms.model.ResultsSummary;
import com.stec.srms.model.StudentInfo;
import com.stec.srms.util.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FacultyDBHandler extends Database {
    private static FacultyDBHandler facultyDBHandlerInstance;
    Context context;

    public FacultyDBHandler(Context context) {
        super(context);
        this.context = context;
    }

    public static synchronized FacultyDBHandler getInstance(Context context) {
        if (facultyDBHandlerInstance == null)
            facultyDBHandlerInstance = new FacultyDBHandler(context.getApplicationContext());
        return facultyDBHandlerInstance;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM faculties WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                db.close();
                cursor.close();
                return true;
            }

            query = "SELECT * FROM pending_faculties WHERE email = '" + email + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean hasSemesterInResultSummary(int sessionId, int deptId, int semesterId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM results_summary_" + sessionId + "_" + deptId + " WHERE semesterId = " + semesterId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public void regenerateGpa(Context context, int sessionId, int deptId, int studentId, int semesterId) {
        ArrayList<CourseInfo> courses = getSemesterCourses(semesterId);
        ArrayList<Results> results = StudentDBHandler.getInstance(context).getResults(sessionId, deptId, studentId, semesterId);
        Map<Integer, Double> courseCreditMap = new HashMap<>();
        for (CourseInfo course : courses) {
            courseCreditMap.put(course.courseCode, course.credit);
        }

        double totalWeightedGpa = 0.0;
        double totalCredits = 0.0;
        for (Results result : results) {
            Double credit = courseCreditMap.get(result.courseCode);
            if (credit == null) continue;
            totalWeightedGpa += result.gpa * credit;
            totalCredits += credit;
        }
        @SuppressLint("DefaultLocale")
        double gpa = Double.parseDouble(String.format("%.2f", totalCredits > 0 ? totalWeightedGpa / totalCredits : 0.0));

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("gpa", gpa);
            db.update(
                    "results_summary_" + sessionId + "_" + deptId, values,
                    "studentId = ? AND semesterId = ?",
                    new String[]{String.valueOf(studentId), String.valueOf(semesterId)}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewResult(Context context, int sessionId, int deptId, Results result) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("studentId", result.studentId);
            values.put("semesterId", result.semesterId);
            values.put("courseCode", result.courseCode);
            values.put("mark", result.mark);
            values.put("gpa", result.gpa);
            db.insert("results_" + sessionId + "_" + deptId, null, values);
        } catch (Exception e) {
            Toast.databaseError(context, "Failed to add result for student " + result.studentId);
        }
    }

    public void addNewResultSummary(Context context, int sessionId, int deptId, ResultsSummary resultsSummary) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("studentId", resultsSummary.studentId);
            values.put("semesterId", resultsSummary.semesterId);
            values.put("gpa", resultsSummary.gpa);
            db.insert("results_summary_" + sessionId + "_" + deptId, null, values);
        } catch (Exception e) {
            Toast.databaseError(context, "Failed to add result summary for student " + resultsSummary.studentId);
        }
    }

    public void addNewCourseResult(Context context, int sessionId, int deptId, int semesterId, int courseCode) {
        ArrayList<StudentInfo> students = StudentDBHandler.getInstance(context).getStudents(deptId, sessionId);
        if (students == null || students.isEmpty()) return;
        for (StudentInfo student : students) {
            addNewResult(context, sessionId, deptId, new Results(student.studentId, semesterId, courseCode, 0, 0.0));
        }
    }

    public void addNewCourseResultSummary(Context context, int sessionId, int deptId, int semesterId) {
        ArrayList<StudentInfo> students = StudentDBHandler.getInstance(context).getStudents(deptId, sessionId);
        if (students == null || students.isEmpty()) return;
        for (StudentInfo student : students) {
            addNewResultSummary(context, sessionId, deptId, new ResultsSummary(student.studentId, semesterId, 0.0));
        }
    }

    public void createNewResultTable(Context context, int sessionId, int deptId, int semesterId, int courseCode) {
        createResultsTable(sessionId, deptId, null);
        createResultsSummaryTable(sessionId, deptId, null);
        addNewCourseResult(context, sessionId, deptId, semesterId, courseCode);
        addNewCourseResultSummary(context, sessionId, deptId, semesterId);
    }

    public FacultyInfo getFacultyInfo(int facultyId) {
        FacultyInfo info = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM faculties WHERE facultyId = " + facultyId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                info = new FacultyInfo();
                info.facultyId = cursor.getInt(cursor.getColumnIndexOrThrow("facultyId"));
                info.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                info.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                info.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                info.contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                info.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                info.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
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

    public ArrayList<Results> getCourseResult(int sessionId, int deptId, int courseCode) {
        ArrayList<Results> results = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM results_" + sessionId + "_" + deptId + " WHERE courseCode = " + courseCode + " ORDER BY studentId ASC;";
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
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return results;
    }

    public void updateResult(Context context, int sessionId, int deptId, Results result) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("mark", result.mark);
            values.put("gpa", result.gpa);
            String where = "studentId = " + result.studentId + " AND courseCode = " + result.courseCode + " AND semesterId = " + result.semesterId;
            int count = db.update("results_" + sessionId + "_" + deptId, values, where, null);
            if (count == 0) {
                Toast.databaseError(context, "Failed to update result");
            }
        } catch (Exception e) {
            Toast.databaseError(context, "Failed to update result");
        }
    }

    public int addPendingFaculty(PendingFaculty facultyInfo) {
        int accountId = getAccountType("pendingFaculty").accountId;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("name", facultyInfo.name);
            values.put("gender", facultyInfo.gender);
            values.put("deptId", facultyInfo.deptId);
            values.put("contact", facultyInfo.contact);
            values.put("email", facultyInfo.email);
            values.put("address", facultyInfo.address);
            values.put("password", facultyInfo.password);
            int userId = (int) db.insert("pending_faculties", null, values);
            if (userId == -1) {
                Toast.databaseError(context, "Failed to add faculty record");
                return -1;
            }

            values = new ContentValues();
            values.put("accountId", accountId);
            values.put("userId", userId);
            int verificationId = (int) db.insert("pending_verifications", null, values);
            if (verificationId == -1) {
                Toast.databaseError(context, "Failed to add faculty record");
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
