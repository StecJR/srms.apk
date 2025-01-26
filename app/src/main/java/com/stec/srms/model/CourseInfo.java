package com.stec.srms.model;

public class CourseInfo {
    public int deptId;
    public int semesterId;
    public int courseCode;
    public double credit;
    public String shortDesc;
    public String longDesc;

    public CourseInfo() {}
    public CourseInfo(int deptId, int semesterId, int courseCode, double credit, String shortDesc, String longDesc) {
        this.deptId = deptId;
        this.semesterId = semesterId;
        this.courseCode = courseCode;
        this.credit = credit;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public static String getQuery() {
        return "CREATE TABLE course_info" +
                " (" +
                "deptId INTEGER, " +
                "semesterId INTEGER, " +
                "courseCode INTEGER, " +
                "credit REAL, " +
                "shortDesc TEXT, " +
                "longDesc TEXT, " +
                "PRIMARY KEY (deptId, courseCode), " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId), " +
                "FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId)" +
                ");";
    }
}
