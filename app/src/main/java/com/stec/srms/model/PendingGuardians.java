package com.stec.srms.model;

public class PendingGuardians {
    public int userId;
    public String name;
    public String contact;
    public String email;
    public int studentId;
    public int deptId;
    public String password;

    public PendingGuardians() {}
    public PendingGuardians(int userId, String name, String contact, String email, int studentId, int deptId, String password) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.studentId = studentId;
        this.deptId = deptId;
        this.password = password;
    }

    public static String getQuery() {
        return "CREATE TABLE pending_guardians" +
                " (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "studentId INTEGER, " +
                "deptId INTEGER, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId)" +
                ");";
    }
}
