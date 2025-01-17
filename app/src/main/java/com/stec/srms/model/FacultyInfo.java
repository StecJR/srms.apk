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

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE faculties")
                .append(" (")
                .append("facultyId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT, ")
                .append("gender TEXT, ")
                .append("deptId INTEGER, ")
                .append("contact TEXT, ")
                .append("email TEXT UNIQUE, ")
                .append("address TEXT, ")
                .append("password TEXT, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId)")
                .append(");");
        return query.toString();
    }

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
}
