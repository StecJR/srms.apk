package com.stec.srms.model;

public class PendingStudents {
    public int userId;
    public String name;
    public String birthDate;
    public String gender;
    public int deptId;
    public int sessionId;
    public String contact;
    public String email;
    public String address;
    public String password;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE pending_students")
                .append(" (")
                .append("userId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT, ")
                .append("birthDate TEXT, ")
                .append("gender TEXT, ")
                .append("deptId INTEGER, ")
                .append("sessionId INTEGER, ")
                .append("contact TEXT, ")
                .append("email TEXT UNIQUE, ")
                .append("address TEXT, ")
                .append("password TEXT, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId), ")
                .append("FOREIGN KEY (sessionId) REFERENCES session_info(sessionId)")
                .append(");");
        return query.toString();
    }

    public PendingStudents() {
    }

    public PendingStudents(int userId, String name, String birthDate, String gender, int deptId, int sessionId, String contact, String email, String address, String password) {
        this.userId = userId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.deptId = deptId;
        this.sessionId = sessionId;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.password = password;
    }
}
