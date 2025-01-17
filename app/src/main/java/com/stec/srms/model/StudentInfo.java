package com.stec.srms.model;

public class StudentInfo {
    public int studentId;
    public String name;
    public String birthDate;
    public String gender;
    public int deptId;
    public int sessionId;
    public String contact;
    public String email;
    public String address;
    public int guardianId;
    public String password;

    public static String getQuery(Integer deptId) {
        StringBuilder query = new StringBuilder("CREATE TABLE students_" + deptId)
                .append(" (")
                .append("studentId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT, ")
                .append("birthDate TEXT, ")
                .append("gender TEXT, ")
                .append("deptId INTEGER, ")
                .append("sessionId INTEGER, ")
                .append("contact TEXT, ")
                .append("email TEXT UNIQUE, ")
                .append("address TEXT, ")
                .append("guardianId INTEGER DEFAULT -1, ")
                .append("password TEXT, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId), ")
                .append("FOREIGN KEY (sessionId) REFERENCES session_info(sessionId), ")
                .append("FOREIGN KEY (guardianId) REFERENCES guardians(guardianId)")
                .append(");");
        return query.toString();
    }

    public StudentInfo() {
    }

    public StudentInfo(int studentId, String name, String birthDate, String gender, int deptId, int sessionId, String contact, String email, String address, int guardianId, String password) {
        this.studentId = studentId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.deptId = deptId;
        this.sessionId = sessionId;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.guardianId = guardianId;
        this.password = password;
    }
}
