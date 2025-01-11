package com.stec.srms.model;

public class FacultyInfo {
    public Integer facultyId;
    public String name;
    public String gender;
    public Integer deptId;
    public String contact;
    public String email;
    public String address;
    public String password;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE faculties")
                .append(" (")
                .append("facultyId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT NOT NULL, ")
                .append("gender TEXT NOT NULL, ")
                .append("deptId INTEGER NOT NULL, ")
                .append("contact TEXT NOT NULL, ")
                .append("email TEXT NOT NULL UNIQUE, ")
                .append("address TEXT NOT NULL, ")
                .append("password TEXT NOT NULL, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId)")
                .append(")");
        return query.toString();
    }
}
