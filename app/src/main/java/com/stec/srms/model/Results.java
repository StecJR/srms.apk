package com.stec.srms.model;

public class Results {
    public Integer studentId;
    public Integer semesterId;
    public String courseCode;
    public Integer mark;
    public double gpa;

    public static String getQuery(Integer sessionId, Integer deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE results_" + sessionId + "_" + deptId)
                .append(" (")
                .append("studentId INTEGER, ")
                .append("semesterId INTEGER, ")
                .append("courseCode TEXT, ")
                .append("mark INTEGER, ")
                .append("gpa REAL, ")

                .append("PRIMARY KEY (studentId, courseCode), ")
                .append("FOREIGN KEY (studentId) REFERENCES students_" + deptId + "(studentId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId), ")
                .append("FOREIGN KEY (courseCode) REFERENCES course_info(courseCode)")
                .append(")");
        return query.toString();
    }
}
