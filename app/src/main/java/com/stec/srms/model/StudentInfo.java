package com.stec.srms.model;

public class StudentInfo {
    public Integer studentId;
    public String name;
    public String birthDate;
    public String gender;
    public Integer deptId;
    public Integer sessionId;
    public String contact;
    public String email;
    public String address;
    public Integer guardianId;
    public String password;

    public static String getQuery(Integer deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE students_" + deptId)
            .append(" (")
            .append("studentId INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append("name TEXT NOT NULL, ")
            .append("birthDate TEXT NOT NULL, ")
            .append("gender TEXT NOT NULL, ")
            .append("deptId INTEGER NOT NULL, ")
            .append("sessionId INTEGER NOT NULL, ")
            .append("contact TEXT NOT NULL, ")
            .append("email TEXT NOT NULL UNIQUE, ")
            .append("address TEXT NOT NULL, ")
            .append("guardianId INTEGER, ")
            .append("password TEXT NOT NULL, ")

            .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId), ")
            .append("FOREIGN KEY (sessionId) REFERENCES session_info(sessionId), ")
            .append("FOREIGN KEY (guardianId) REFERENCES guardians(guardianId)")
            .append(")");
        return query.toString();
    }
}
