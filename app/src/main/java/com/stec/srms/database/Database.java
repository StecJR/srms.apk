package com.stec.srms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.stec.srms.model.AccountType;
import com.stec.srms.model.AdminInfo;
import com.stec.srms.model.CourseInfo;
import com.stec.srms.model.DeptInfo;
import com.stec.srms.model.FacultyInfo;
import com.stec.srms.model.GuardianInfo;
import com.stec.srms.model.PendingFaculties;
import com.stec.srms.model.PendingGuardians;
import com.stec.srms.model.PendingStudents;
import com.stec.srms.model.PendingVerifications;
import com.stec.srms.model.Results;
import com.stec.srms.model.ResultsSummary;
import com.stec.srms.model.SemesterInfo;
import com.stec.srms.model.SessionInfo;
import com.stec.srms.model.StudentInfo;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static Database databaseInstance;
    private static final String DATABASE_NAME = "SRMS";
    private static final int DATABASE_VERSION = 1;

    private static ArrayList<AccountType> accountTypes = null;
    private static ArrayList<DeptInfo> departments = null;
    private static ArrayList<SessionInfo> sessions = null;
    private static ArrayList<CourseInfo> courses = null;
    private static ArrayList<SemesterInfo> semesters = null;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized Database getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = new Database(context.getApplicationContext());
        }
        return databaseInstance;
    }

    // Create: static tables
    public void createAccountTypesTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(AccountType.getQuery());
    }

    public void createDeptInfoTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(DeptInfo.getQuery());
    }

    public void createSessionInfoTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(SessionInfo.getQuery());
    }

    public void createSemesterInfoTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(SemesterInfo.getQuery());
    }

    public void createCourseInfoTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(CourseInfo.getQuery());
    }

    public void createAdminInfoTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(AdminInfo.getQuery());
    }

    // Create: user tables
    public void createStudentsTable(int deptId, SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(StudentInfo.getQuery(deptId));
    }

    public void createFacultiesTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(FacultyInfo.getQuery());
    }

    public void createGuardiansTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(GuardianInfo.getQuery());
    }

    // Create: result tables
    public void createResultsSummaryTable(int sessionId, int deptId, SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(ResultsSummary.getQuery(sessionId, deptId));
    }

    public void createResultsTable(int sessionId, int deptId, SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(Results.getQuery(sessionId, deptId));
    }

    // Create: pending verifications tables
    public void createPendingVerificationsTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(PendingVerifications.getQuery());
    }

    public void createPendingStudentsTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(PendingStudents.getQuery());
    }

    public void createPendingFacultiesTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(PendingFaculties.getQuery());
    }

    public void createPendingGuardiansTable(SQLiteDatabase db) {
        if (db == null) db = this.getWritableDatabase();
        db.execSQL(PendingGuardians.getQuery());
    }

    // Get: static table data
    public AccountType getAccountType(int id) {
        if (accountTypes != null) {
            for (AccountType item : accountTypes) {
                if (item.accountId == id) return item;
            }
        }
        AccountType accountType = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM account_types WHERE accountId = " + id + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                accountType = new AccountType();
                accountType.accountId = cursor.getInt(cursor.getColumnIndexOrThrow("accountId"));
                accountType.accountType = cursor.getString(cursor.getColumnIndexOrThrow("accountType"));
                return accountType;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return accountType;
    }

    public AccountType getAccountType(String type) {
        if (accountTypes != null) {
            for (AccountType item : accountTypes) {
                if (item.accountType.equals(type)) return item;
            }
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        AccountType accountType = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM account_types WHERE accountType = '" + type + "' LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                accountType = new AccountType();
                accountType.accountId = cursor.getInt(cursor.getColumnIndexOrThrow("accountId"));
                accountType.accountType = cursor.getString(cursor.getColumnIndexOrThrow("accountType"));
                return accountType;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return accountType;
    }

    public ArrayList<AccountType> getAccountTypes() {
        if (accountTypes != null) return accountTypes;
        ArrayList<AccountType> dbAccountTypes = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM account_types;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dbAccountTypes = new ArrayList<>();
                do {
                    AccountType accountType = new AccountType();
                    accountType.accountId = cursor.getInt(cursor.getColumnIndexOrThrow("accountId"));
                    accountType.accountType = cursor.getString(cursor.getColumnIndexOrThrow("accountType"));
                    dbAccountTypes.add(accountType);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        accountTypes = dbAccountTypes;
        return accountTypes;
    }

    public DeptInfo getDepartment(int deptId) {
        if (departments != null) {
            for (DeptInfo item : departments) {
                if (item.deptId == deptId) return item;
            }
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        DeptInfo deptInfo = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM dept_info WHERE deptId = " + deptId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                deptInfo = new DeptInfo();
                deptInfo.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                deptInfo.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                deptInfo.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return deptInfo;
    }

    public ArrayList<DeptInfo> getDepartments() {
        if (departments != null) return departments;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<DeptInfo> dbDepartments = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM dept_info;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dbDepartments = new ArrayList<>();
                do {
                    DeptInfo department = new DeptInfo();
                    department.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    department.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    department.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    dbDepartments.add(department);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        departments = dbDepartments;
        return departments;
    }

    public SessionInfo getSession(int sessionId) {
        if (sessions != null) {
            for (SessionInfo item : sessions) {
                if (item.sessionId == sessionId) return item;
            }
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        SessionInfo sessionInfo = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM session_info WHERE sessionId = " + sessionId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                sessionInfo = new SessionInfo();
                sessionInfo.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                sessionInfo.desc = cursor.getString(cursor.getColumnIndexOrThrow("desc"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return sessionInfo;
    }

    public ArrayList<SessionInfo> getSessions() {
        if (sessions != null) return sessions;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<SessionInfo> dbSessions = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM session_info;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dbSessions = new ArrayList<>();
                do {
                    SessionInfo session = new SessionInfo();
                    session.sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"));
                    session.desc = cursor.getString(cursor.getColumnIndexOrThrow("desc"));
                    dbSessions.add(session);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        sessions = dbSessions;
        return sessions;
    }

    public CourseInfo getCourse(int courseCode) {
        if (courses != null) {
            for (CourseInfo item : courses) {
                if (item.courseCode == courseCode) return item;
            }
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        CourseInfo course = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM course_info WHERE courseCode = " + courseCode + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                course = new CourseInfo();
                course.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                course.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                course.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                course.credit = cursor.getDouble(cursor.getColumnIndexOrThrow("credit"));
                course.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                course.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return course;
    }

    public ArrayList<CourseInfo> getCourses() {
        if (courses != null) return courses;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<CourseInfo> dbCourses = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM course_info;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dbCourses = new ArrayList<>();
                do {
                    CourseInfo course = new CourseInfo();
                    course.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    course.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    course.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    course.credit = cursor.getDouble(cursor.getColumnIndexOrThrow("credit"));
                    course.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    course.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    dbCourses.add(course);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        courses = dbCourses;
        return courses;
    }

    public ArrayList<CourseInfo> getDepartmentCourses(int deptId) {
        ArrayList<CourseInfo> deptCourses = new ArrayList<>();
        if (courses != null) {
            for (CourseInfo item : courses) {
                if (item.deptId == deptId) deptCourses.add(item);
            }
            return deptCourses;
        };
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM course_info WHERE deptId = " + deptId + ";";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    CourseInfo course = new CourseInfo();
                    course.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    course.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    course.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    course.credit = cursor.getDouble(cursor.getColumnIndexOrThrow("credit"));
                    course.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    course.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    deptCourses.add(course);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return deptCourses;
    }

    public ArrayList<CourseInfo> getSemesterCourses(int semesterId) {
        ArrayList<CourseInfo> semesterCourses = new ArrayList<>();
        if (courses != null) {
            for (CourseInfo item : courses) {
                if (item.semesterId == semesterId) semesterCourses.add(item);
            }
            return semesterCourses;
        };
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM course_info WHERE semesterId = " + semesterId + ";";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    CourseInfo course = new CourseInfo();
                    course.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    course.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    course.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    course.credit = cursor.getDouble(cursor.getColumnIndexOrThrow("credit"));
                    course.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    course.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    semesterCourses.add(course);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return semesterCourses;
    }

    public ArrayList<CourseInfo> getSemesterCourses(int deptId, int semesterId) {
        ArrayList<CourseInfo> semesterCourses = new ArrayList<>();
        if (courses != null) {
            for (CourseInfo item : courses) {
                if (item.deptId == deptId && item.semesterId == semesterId) semesterCourses.add(item);
            }
            return semesterCourses;
        };
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM course_info WHERE deptId = " + deptId + " AND semesterId = " + semesterId + ";";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    CourseInfo course = new CourseInfo();
                    course.deptId = cursor.getInt(cursor.getColumnIndexOrThrow("deptId"));
                    course.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    course.courseCode = cursor.getInt(cursor.getColumnIndexOrThrow("courseCode"));
                    course.credit = cursor.getDouble(cursor.getColumnIndexOrThrow("credit"));
                    course.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    course.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    semesterCourses.add(course);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return semesterCourses;
    }

    public SemesterInfo getSemester(int semesterId) {
        if (semesters != null) {
            for (SemesterInfo item : semesters) {
                if (item.semesterId == semesterId) return item;
            }
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        SemesterInfo semesterInfo = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM semester_info" + " WHERE semesterId = " + semesterId + " LIMIT 1;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                semesterInfo = new SemesterInfo();
                semesterInfo.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                semesterInfo.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                semesterInfo.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return semesterInfo;
    }

    public ArrayList<SemesterInfo> getSemesters() {
        if (semesters != null) return semesters;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<SemesterInfo> dbSemesters = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM semester_info;";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dbSemesters = new ArrayList<>();
                do {
                    SemesterInfo semester = new SemesterInfo();
                    semester.semesterId = cursor.getInt(cursor.getColumnIndexOrThrow("semesterId"));
                    semester.shortDesc = cursor.getString(cursor.getColumnIndexOrThrow("shortDesc"));
                    semester.longDesc = cursor.getString(cursor.getColumnIndexOrThrow("longDesc"));
                    dbSemesters.add(semester);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        semesters = dbSemesters;
        return semesters;
    }

    // Verify: users
    public boolean isValidStudent(int deptId, int studentId) {
        if (studentId == -1) return false;
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId + " LIMIT 1;";
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

    public boolean isValidStudent(int deptId, int studentId, String studentPw) {
        if (studentId == -1) return false;
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM students_" + deptId + " WHERE studentId = " + studentId + " AND password = '" + studentPw + "' LIMIT 1;";
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

    public boolean isValidGuardian(int guardianId) {
        if (guardianId == -1) return false;
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM guardians WHERE guardianId = " + guardianId + " LIMIT 1;";
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

    public boolean isValidGuardian(int guardianId, String guardianPw) {
        if (guardianId == -1) return false;
        boolean isValid = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM guardians WHERE guardianId = " + guardianId + " AND password = '" + guardianPw + "' LIMIT 1;";
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

    // Add: dummy data
    public void addDummyStaticData(@NonNull SQLiteDatabase db) {
        StringBuilder query;
        // Account types table
        query = new StringBuilder("INSERT INTO account_types VALUES")
                .append(" (1, 'admin'),")
                .append(" (2, 'student'),")
                .append(" (3, 'faculty'),")
                .append(" (4, 'guardian'),")
                .append(" (5, 'pendingStudent'),")
                .append(" (6, 'pendingFaculty'),")
                .append(" (7, 'pendingGuardian');");
        db.execSQL(query.toString());
        // Department type table
        query = new StringBuilder("INSERT INTO dept_info VALUES")
                .append(" (1, 'CSE', 'Computer Science and Engineering'),")
                .append(" (2, 'EEE', 'Electrical and Electronics Engineering');");
        db.execSQL(query.toString());
        // Session info table
        query = new StringBuilder("INSERT INTO session_info VALUES")
                .append(" (22, '2021-2022'),")
                .append(" (23, '2022-2023'),")
                .append(" (24, '2023-2024');");
        db.execSQL(query.toString());
        // Semester info table
        query = new StringBuilder("INSERT INTO semester_info VALUES")
                .append(" (11, '1Y 1S', '1st Year 1st Semester'),")
                .append(" (12, '1Y 2S', '1st Year 2nd Semester'),")
                .append(" (21, '2Y 1S', '2nd Year 1st Semester'),")
                .append(" (22, '2Y 2S', '2nd Year 2nd Semester'),")
                .append(" (31, '3Y 1S', '3rd Year 1st Semester'),")
                .append(" (32, '3Y 2S', '3rd Year 2nd Semester'),")
                .append(" (41, '4Y 1S', '4th Year 1st Semester'),")
                .append(" (42, '4Y 2S', '4th Year 2nd Semester');");
        db.execSQL(query.toString());
        // Course info table
        query = new StringBuilder("INSERT INTO course_info VALUES")
                .append(" (1, 11, 1101, 2.0, 'FCC', 'Fundamentals of Computers and Computing'),")
                .append(" (1, 11, 1102, 3.0, 'DM', 'Discrete Mathematics'),")
                .append(" (1, 11, 1103, 3.0, 'EC', 'Electrical Circuits'),")
                .append(" (1, 11, 1104, 3.0, 'Chem', 'Chemistry'),")
                .append(" (1, 11, 1105, 3.0, 'DIC', 'Differential and Integral Calculus'),")
                .append(" (1, 11, 1111, 1.5, 'FCC Lab', 'Fundamentals of Computers and Computing Lab'),")
                .append(" (1, 11, 1113, 1.5, 'EC Lab', 'Electrical Circuits Lab'),")
                .append(" (1, 11, 1114, 1.5, 'Chem Lab', 'Chemistry Lab'),")

                .append(" (1, 12, 1201, 3.0, 'FP', 'Fundamentals of Programming'),")
                .append(" (1, 12, 1202, 3.0, 'DLD', 'Digital Logic Design'),")
                .append(" (1, 12, 1203, 3.0, 'Phy', 'Physics'),")
                .append(" (1, 12, 1204, 3.0, 'MIDE', 'Methods of Integration, Differential Equations'),")
                .append(" (1, 12, 1205, 2.0, 'Eng', 'Developing English Language Skills'),")
                .append(" (1, 12, 1211, 3.0, 'FP Lab', 'Fundamentals of Programming Lab'),")
                .append(" (1, 12, 1212, 1.5, 'DLD Lab', 'Digital Logic Design Lab'),")
                .append(" (1, 12, 1213, 1.5, 'Phy Lab', 'Physics Lab'),")

                .append(" (2, 11, 1101, 2.0, 'FCC', 'Fundamentals of Computers and Computing'),")
                .append(" (2, 11, 1102, 3.0, 'DM', 'Discrete Mathematics'),")
                .append(" (2, 11, 1103, 3.0, 'EC', 'Electrical Circuits'),")
                .append(" (2, 11, 1104, 3.0, 'Chem', 'Chemistry'),")
                .append(" (2, 11, 1105, 3.0, 'DIC', 'Differential and Integral Calculus'),")
                .append(" (2, 11, 1111, 1.5, 'FCC Lab', 'Fundamentals of Computers and Computing Lab'),")
                .append(" (2, 11, 1113, 1.5, 'EC Lab', 'Electrical Circuits Lab'),")
                .append(" (2, 11, 1114, 1.5, 'Chem Lab', 'Chemistry Lab'),")

                .append(" (2, 12, 1201, 3.0, 'FP', 'Fundamentals of Programming'),")
                .append(" (2, 12, 1202, 3.0, 'DLD', 'Digital Logic Design'),")
                .append(" (2, 12, 1203, 3.0, 'Phy', 'Physics'),")
                .append(" (2, 12, 1204, 3.0, 'MIDE', 'Methods of Integration, Differential Equations'),")
                .append(" (2, 12, 1205, 2.0, 'Eng', 'Developing English Language Skills'),")
                .append(" (2, 12, 1211, 3.0, 'FP Lab', 'Fundamentals of Programming Lab'),")
                .append(" (2, 12, 1212, 1.5, 'DLD Lab', 'Digital Logic Design Lab'),")
                .append(" (2, 12, 1213, 1.5, 'Phy Lab', 'Physics Lab');");
        db.execSQL(query.toString());
        // Admin info table
        query = new StringBuilder("INSERT INTO admin_info VALUES")
                .append(" ('admin', 'admin');");
        db.execSQL(query.toString());
    }

    public void addDummyUserData(@NonNull SQLiteDatabase db) {
        StringBuilder query;
        // CSE student table
        query = new StringBuilder("INSERT INTO students_1 VALUES")
                .append(" (10000001, 'Jakir Hossain', '9 February 2004', 'Male', 1, 22, '01836123456', 'stec.jrhossain@gmail.com', 'Dhaka, Bangladesh', 10000001, '1234'),")
                .append(" (10000002, 'Anik Kumara', '9 March 2004', 'Male', 1, 22, '01836123457', 'stec.anikkumar@gmail.com', 'Dhaka, Bangladesh', -1, '1234');");
        db.execSQL(query.toString());
        // EEE student table
        query = new StringBuilder("INSERT INTO students_2 VALUES")
                .append(" (20000001, 'Jakir Hossain', '9 February 2004', 'Male', 2, 22, '01836123456', 'stec.jrhossain@gmail.com', 'Dhaka, Bangladesh', -1, '1234'),")
                .append(" (20000002, 'Anik Kumara', '9 March 2004', 'Male', 2, 22, '01836123457', 'stec.anikkumar@gmail.com', 'Dhaka, Bangladesh', 10000002, '1234');");
        db.execSQL(query.toString());
        // Faculty table
        query = new StringBuilder("INSERT INTO faculties VALUES")
                .append(" (10000001, 'Jakir Hossain', 'Male', 1, '01836123456', 'stec.jrhossain@gmail.com', 'Dhaka, Bangladesh', '1234'),")
                .append(" (10000002, 'Anik Kumara', 'Male', 1, '01836123457', 'stec.anikkumar@gmail.com', 'Dhaka, Bangladesh', '1234'),")
                .append(" (10000003, 'Jakir Hossain', 'Male', 2, '01836123456', 'stec.jrhossain2@gmail.com', 'Dhaka, Bangladesh', '1234'),")
                .append(" (10000004, 'Anik Kumara', 'Male', 2, '01836123457', 'stec.anikkumar2@gmail.com', 'Dhaka, Bangladesh', '1234');");
        db.execSQL(query.toString());
        // Guardians table
        query = new StringBuilder("INSERT INTO guardians VALUES")
                .append(" (10000001, 'Jakir Hossain', 'Brother', '01836123456', 'stec.jrhossain@gmail.com', 10000001, 1, '1234'),")
                .append(" (10000002, 'Anik Kumara', 'Father', '01836123457', 'stec.anikkumar@gmail.com', 20000002, 2, '1234');");
        db.execSQL(query.toString());
    }

    public void addDummyResultData(@NonNull SQLiteDatabase db) {
        StringBuilder query;
        // CSE student results summary table
        query = new StringBuilder("INSERT INTO results_summary_22_1 VALUES")
                .append(" (10000001, 11, 3.5, 3.5),")
                .append(" (10000001, 12, 3.5, 3.5),")
                .append(" (10000002, 11, 3.5, 3.5),")
                .append(" (10000002, 12, 3.5, 3.5);");
        db.execSQL(query.toString());
        // EEE student results summary table
        query = new StringBuilder("INSERT INTO results_summary_22_2 VALUES")
                .append(" (20000001, 11, 3.5, 3.5),")
                .append(" (20000001, 12, 3.5, 3.5),")
                .append(" (20000002, 11, 3.5, 3.5),")
                .append(" (20000002, 12, 3.5, 3.5);");
        db.execSQL(query.toString());

        // CSE student results table
        query = new StringBuilder("INSERT INTO results_22_1 VALUES")
                .append(" (10000001, 11, 1101, 76, 3.5),")
                .append(" (10000001, 11, 1102, 76, 3.5),")
                .append(" (10000001, 11, 1103, 76, 3.5),")
                .append(" (10000001, 11, 1104, 76, 3.5),")
                .append(" (10000001, 11, 1105, 76, 3.5),")
                .append(" (10000001, 11, 1111, 76, 3.5),")
                .append(" (10000001, 11, 1113, 76, 3.5),")
                .append(" (10000001, 11, 1114, 76, 3.5),")
                .append(" (10000002, 11, 1101, 76, 3.5),")
                .append(" (10000002, 11, 1102, 76, 3.5),")
                .append(" (10000002, 11, 1103, 76, 3.5),")
                .append(" (10000002, 11, 1104, 76, 3.5),")
                .append(" (10000002, 11, 1105, 76, 3.5),")
                .append(" (10000002, 11, 1111, 76, 3.5),")
                .append(" (10000002, 11, 1113, 76, 3.5),")
                .append(" (10000002, 11, 1114, 76, 3.5),")

                .append(" (10000001, 12, 1201, 76, 3.5),")
                .append(" (10000001, 12, 1202, 76, 3.5),")
                .append(" (10000001, 12, 1203, 76, 3.5),")
                .append(" (10000001, 12, 1204, 76, 3.5),")
                .append(" (10000001, 12, 1205, 76, 3.5),")
                .append(" (10000001, 12, 1211, 76, 3.5),")
                .append(" (10000001, 12, 1212, 76, 3.5),")
                .append(" (10000001, 12, 1213, 76, 3.5),")
                .append(" (10000002, 12, 1201, 76, 3.5),")
                .append(" (10000002, 12, 1202, 76, 3.5),")
                .append(" (10000002, 12, 1203, 76, 3.5),")
                .append(" (10000002, 12, 1204, 76, 3.5),")
                .append(" (10000002, 12, 1205, 76, 3.5),")
                .append(" (10000002, 12, 1211, 76, 3.5),")
                .append(" (10000002, 12, 1212, 76, 3.5),")
                .append(" (10000002, 12, 1213, 76, 3.5);");
        db.execSQL(query.toString());

        // EEE student results table
        query = new StringBuilder("INSERT INTO results_22_2 VALUES")
                .append(" (20000001, 11, 1101, 76, 3.5),")
                .append(" (20000001, 11, 1102, 76, 3.5),")
                .append(" (20000001, 11, 1103, 76, 3.5),")
                .append(" (20000001, 11, 1104, 76, 3.5),")
                .append(" (20000001, 11, 1105, 76, 3.5),")
                .append(" (20000001, 11, 1111, 76, 3.5),")
                .append(" (20000001, 11, 1113, 76, 3.5),")
                .append(" (20000001, 11, 1114, 76, 3.5),")
                .append(" (20000002, 11, 1101, 76, 3.5),")
                .append(" (20000002, 11, 1102, 76, 3.5),")
                .append(" (20000002, 11, 1103, 76, 3.5),")
                .append(" (20000002, 11, 1104, 76, 3.5),")
                .append(" (20000002, 11, 1105, 76, 3.5),")
                .append(" (20000002, 11, 1111, 76, 3.5),")
                .append(" (20000002, 11, 1113, 76, 3.5),")
                .append(" (20000002, 11, 1114, 76, 3.5),")

                .append(" (20000001, 12, 1201, 76, 3.5),")
                .append(" (20000001, 12, 1202, 76, 3.5),")
                .append(" (20000001, 12, 1203, 76, 3.5),")
                .append(" (20000001, 12, 1204, 76, 3.5),")
                .append(" (20000001, 12, 1205, 76, 3.5),")
                .append(" (20000001, 12, 1211, 76, 3.5),")
                .append(" (20000001, 12, 1212, 76, 3.5),")
                .append(" (20000001, 12, 1213, 76, 3.5),")
                .append(" (20000002, 12, 1201, 76, 3.5),")
                .append(" (20000002, 12, 1202, 76, 3.5),")
                .append(" (20000002, 12, 1203, 76, 3.5),")
                .append(" (20000002, 12, 1204, 76, 3.5),")
                .append(" (20000002, 12, 1205, 76, 3.5),")
                .append(" (20000002, 12, 1211, 76, 3.5),")
                .append(" (20000002, 12, 1212, 76, 3.5),")
                .append(" (20000002, 12, 1213, 76, 3.5);");
        db.execSQL(query.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createAccountTypesTable(db);
        this.createDeptInfoTable(db);
        this.createSessionInfoTable(db);
        this.createCourseInfoTable(db);
        this.createSemesterInfoTable(db);
        this.createAdminInfoTable(db);
        this.addDummyStaticData(db);

        this.createStudentsTable(1, db);
        this.createStudentsTable(2, db);
        this.createFacultiesTable(db);
        this.createGuardiansTable(db);
        this.addDummyUserData(db);

        this.createResultsSummaryTable(22, 1, db);
        this.createResultsTable(22, 1, db);
        this.createResultsSummaryTable(22, 2, db);
        this.createResultsTable(22, 2, db);
        this.addDummyResultData(db);

        this.createPendingVerificationsTable(db);
        this.createPendingStudentsTable(db);
        this.createPendingFacultiesTable(db);
        this.createPendingGuardiansTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {}
//        if (oldVersion < 3) {}
    }
}
