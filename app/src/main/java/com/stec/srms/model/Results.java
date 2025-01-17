package com.stec.srms.model;

public class Results {
    public int studentId;
    public int semesterId;
    public int courseCode;
    public int mark;
    public double gpa;

    public static String getQuery(int sessionId, int deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE results_" + sessionId + "_" + deptId)
                .append(" (")
                .append("studentId INTEGER, ")
                .append("semesterId INTEGER, ")
                .append("courseCode INTEGER, ")
                .append("mark INTEGER, ")
                .append("gpa REAL, ")

                .append("PRIMARY KEY (studentId, courseCode), ")
                .append("FOREIGN KEY (studentId) REFERENCES students_" + deptId + "(studentId), ")
                .append("FOREIGN KEY (semesterId) REFERENCES semester_info(semesterId), ")
                .append("FOREIGN KEY (courseCode) REFERENCES course_info(courseCode)")
                .append(");");
        return query.toString();
    }

    public Results() {
    }

    public Results(int studentId, int semesterId, int courseCode, int mark, double gpa) {
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.courseCode = courseCode;
        this.mark = mark;
        this.gpa = gpa;
    }
}
