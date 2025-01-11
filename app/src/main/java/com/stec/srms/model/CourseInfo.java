package com.stec.srms.model;

public class CourseInfo {
    public Integer deptId;
    public Integer semesterId;
    public String courseCode;
    public double credit;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE course_info")
                .append(" (")
                .append("deptId INTEGER, ")
                .append("semesterId INTEGER NOT NULL, ")
                .append("courseCode TEXT, ")
                .append("credit REAL NOT NULL, ")
                .append("shortDesc TEXT NOT NULL, ")
                .append("longDesc TEXT NOT NULL, ")

                .append("PRIMARY KEY (deptId, courseCode), ")
                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)")
                .append(")");
        return query.toString();
    }
}
