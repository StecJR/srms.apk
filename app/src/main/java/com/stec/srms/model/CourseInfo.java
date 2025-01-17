package com.stec.srms.model;

public class CourseInfo {
    public int deptId;
    public int semesterId;
    public int courseCode;
    public double credit;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE course_info")
                .append(" (")
                .append("deptId INTEGER, ")
                .append("semesterId INTEGER, ")
                .append("courseCode INTEGER, ")
                .append("credit REAL, ")
                .append("shortDesc TEXT, ")
                .append("longDesc TEXT, ")

                .append("PRIMARY KEY (deptId, courseCode), ")
                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)")
                .append(");");
        return query.toString();
    }

    public CourseInfo() {
    }

    public CourseInfo(int deptId, int semesterId, int courseCode, double credit, String shortDesc, String longDesc) {
        this.deptId = deptId;
        this.semesterId = semesterId;
        this.courseCode = courseCode;
        this.credit = credit;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
