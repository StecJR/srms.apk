package com.stec.srms.model;

public class GuardianInfo {
    public Integer guardianId;
    public String name;
    public String contact;
    public String email;
    public Integer studentId;
    public Integer deptId;
    public String password;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE guardians")
                .append(" (")
                .append("guardianId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT NOT NULL, ")
                .append("contact TEXT NOT NULL, ")
                .append("email TEXT NOT NULL UNIQUE, ")
                .append("studentId INTEGER NOT NULL, ")
                .append("deptId INTEGER NOT NULL, ")
                .append("password TEXT NOT NULL, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId)")
                .append(")");
        return query.toString();
    }
}
