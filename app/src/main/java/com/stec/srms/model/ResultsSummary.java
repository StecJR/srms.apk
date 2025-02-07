package com.stec.srms.model;

public class ResultsSummary {
    public int studentId;
    public int semesterId;
    public double gpa;

    public ResultsSummary() {
    }

    public ResultsSummary(int studentId, int semesterId, double gpa) {
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.gpa = gpa;
    }

    public static String getQuery(int sessionId, int deptId) {
        return "CREATE TABLE results_summary_" + sessionId + "_" + deptId +
                " (" +
                "studentId INTEGER, " +
                "semesterId INTEGER, " +
                "gpa REAL, " +
                "PRIMARY KEY (studentId, semesterId), " +
                "FOREIGN KEY (studentId) REFERENCES students_" + deptId + "(studentId), " +
                "FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)" +
                ");";
    }
}
