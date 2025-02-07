package com.stec.srms.model;

public class PendingStudent {
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

    public PendingStudent() {
    }

    public PendingStudent(int userId, String name, String birthDate, String gender, int deptId, int sessionId, String contact, String email, String address, String password) {
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

    public static String getQuery() {
        return "CREATE TABLE pending_students" +
                " (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "birthDate TEXT, " +
                "gender TEXT, " +
                "deptId INTEGER, " +
                "sessionId INTEGER, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "address TEXT, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId), " +
                "FOREIGN KEY (sessionId) REFERENCES session_info(sessionId)" +
                ");";
    }
}
