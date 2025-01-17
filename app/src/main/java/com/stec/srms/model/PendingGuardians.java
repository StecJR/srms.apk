package com.stec.srms.model;

public class PendingGuardians {
    public int userId;
    public String name;
    public String contact;
    public String email;
    public int studentId;
    public int deptId;
    public String password;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE pending_guardians")
                .append(" (")
                .append("userId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT, ")
                .append("contact TEXT, ")
                .append("email TEXT UNIQUE, ")
                .append("studentId INTEGER, ")
                .append("deptId INTEGER, ")
                .append("password TEXT, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId)")
                .append(");");
        return query.toString();
    }

    public PendingGuardians() {
    }

    public PendingGuardians(int userId, String name, String contact, String email, int studentId, int deptId, String password) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.studentId = studentId;
        this.deptId = deptId;
        this.password = password;
    }
}
