package com.stec.srms.model;

public class FacultyInfo {
    public int facultyId;
    public String name;
    public String gender;
    public int deptId;
    public String contact;
    public String email;
    public String address;
    public String password;

    public FacultyInfo() {
    }

    public FacultyInfo(int facultyId, String name, String gender, int deptId, String contact, String email, String address, String password) {
        this.facultyId = facultyId;
        this.name = name;
        this.gender = gender;
        this.deptId = deptId;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public static String getQuery() {
        return "CREATE TABLE faculties" +
                " (" +
                "facultyId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "gender TEXT, " +
                "deptId INTEGER, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "address TEXT, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId)" +
                ");";
    }
}
