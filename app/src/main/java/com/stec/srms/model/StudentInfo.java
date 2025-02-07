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

    public static String getQuery(Integer deptId) {
        return "CREATE TABLE students_" + deptId +
                " (" +
                "studentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "birthDate TEXT, " +
                "gender TEXT, " +
                "deptId INTEGER, " +
                "sessionId INTEGER, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "address TEXT, " +
                "guardianId INTEGER DEFAULT -1, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId), " +
                "FOREIGN KEY (sessionId) REFERENCES session_info(sessionId), " +
                "FOREIGN KEY (guardianId) REFERENCES guardians(guardianId)" +
                ");";
    }
}
