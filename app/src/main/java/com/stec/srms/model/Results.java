package com.stec.srms.model;

public class Results {
    public int studentId;
    public int semesterId;
    public int courseCode;
    public int mark;
    public double gpa;

    public Results() {}
    public Results(int studentId, int semesterId, int courseCode, int mark, double gpa) {
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.courseCode = courseCode;
        this.mark = mark;
        this.gpa = gpa;
    }

    public static String getQuery(int sessionId, int deptId) {
        return "CREATE TABLE results_" + sessionId + "_" + deptId +
                " (" +
                "studentId INTEGER, " +
                "semesterId INTEGER, " +
                "courseCode INTEGER, " +
                "mark INTEGER, " +
                "gpa REAL, " +
                "PRIMARY KEY (studentId, courseCode), " +
                "FOREIGN KEY (studentId) REFERENCES students_" + deptId + "(studentId), " +
                "FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId), " +
                "FOREIGN KEY (courseCode) REFERENCES course_info(courseCode)" +
                ");";
    }
}
