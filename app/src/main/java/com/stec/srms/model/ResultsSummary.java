package com.stec.srms.model;

public class ResultsSummary {
    public int studentId;
    public int semesterId;
    public double gpa;

    public static String getQuery(int sessionId, int deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE results_summary_" + sessionId + "_" + deptId)
                .append(" (")
                .append("studentId INTEGER, ")
                .append("semesterId INTEGER, ")
                .append("gpa REAL, ")

                .append("PRIMARY KEY (studentId, semesterId), ")
                .append("FOREIGN KEY (studentId) REFERENCES students_").append(deptId).append("(studentId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)")
                .append(");");
        return query.toString();
    }

    public ResultsSummary() {
    }

    public ResultsSummary(int studentId, int semesterId, double gpa) {
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.gpa = gpa;
    }
}
