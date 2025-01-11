package com.stec.srms.model;

public class ResultsSummary {
    public Integer studentId;
    public Integer semesterId;
    public double gpa;
    public double cgpa;

    public static String getQuery(Integer sessionId, Integer deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE results_summary_" + sessionId + "_" + deptId)
                .append(" (")
                .append("studentId INTEGER, ")
                .append("semesterId INTEGER, ")
                .append("gpa REAL, ")
                .append("cgpa REAL, ")

                .append("PRIMARY KEY (studentId, semesterId), ")
                .append("FOREIGN KEY (studentId) REFERENCES students_" + deptId + "(studentId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)")
                .append(")");
        return query.toString();
    }
}
